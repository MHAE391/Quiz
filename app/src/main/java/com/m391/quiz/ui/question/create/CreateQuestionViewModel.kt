package com.m391.quiz.ui.question.create

import android.app.Application
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.View
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.m391.quiz.database.local.entities.Question
import com.m391.quiz.ui.shared.BaseViewModel
import com.m391.quiz.utils.Statics.EMPTY_ESSAY_ANSWER
import com.m391.quiz.utils.Statics.EMPTY_MCQ_FIRST_CHOICE
import com.m391.quiz.utils.Statics.EMPTY_MCQ_FOURTH_CHOICE
import com.m391.quiz.utils.Statics.EMPTY_MCQ_SECOND_CHOICE
import com.m391.quiz.utils.Statics.EMPTY_MCQ_THIRD_CHOICE
import com.m391.quiz.utils.Statics.EMPTY_QUESTION_BODY
import com.m391.quiz.utils.Statics.EMPTY_QUESTION_HEADER
import com.m391.quiz.utils.Statics.EMPTY_QUESTION_SCORE
import com.m391.quiz.utils.Statics.EMPTY_QUESTION_TYPE
import com.m391.quiz.utils.Statics.INVALID_QUESTION_SCORE
import com.m391.quiz.utils.Statics.RESPONSE_SUCCESS
import com.m391.quiz.utils.Statics.VALID_DATA
import com.m391.quiz.utils.m391ByteArray
import com.m391.quiz.utils.m391Capitalize
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.IOException

class CreateQuestionViewModel(
    private val app: Application,
    private val quizId: Int,
    private val questionNumber: Int,
    private val insertQuestion: suspend (Question) -> Unit
) :
    BaseViewModel(app) {
    private val _mcqAnswerVisibility = MutableLiveData<Int>()
    val mcqAnswerVisibility: LiveData<Int> = _mcqAnswerVisibility

    private val _essayAnswerVisibility = MutableLiveData<Int>()
    val essayAnswerVisibility: LiveData<Int> = _essayAnswerVisibility

    private val _result = MutableLiveData<String>()
    val result: LiveData<String> = _result

    val questionHeader = MutableLiveData<String>()
    val questionBodyText = MutableLiveData<String>()
    val questionScore = MutableLiveData<String>()

    private val _questionBodyImage = MutableLiveData<String>()
    val questionBodyImage: LiveData<String> = _questionBodyImage

    private val _questionType = MutableLiveData<String>()
    val questionType: LiveData<String> = _questionType

    val firstAnswerText = MutableLiveData<String>()
    val secondAnswerText = MutableLiveData<String>()
    val thirdAnswerText = MutableLiveData<String>()
    val fourthAnswerText = MutableLiveData<String>()

    private val _essayAnswerText = MutableLiveData<Boolean>()
    private val _essayAnswerImage = MutableLiveData<Boolean>()
    val essayAnswerText: LiveData<Boolean> = _essayAnswerText
    val essayAnswerImage: LiveData<Boolean> = _essayAnswerImage
    private val _firstAnswerImage = MutableLiveData<String>()
    private val _secondAnswerImage = MutableLiveData<String>()
    private val _thirdAnswerImage = MutableLiveData<String>()
    private val _fourthAnswerImage = MutableLiveData<String>()

    val firstAnswerImage: LiveData<String> = _firstAnswerImage
    val secondAnswerImage: LiveData<String> = _secondAnswerImage
    val thirdAnswerImage: LiveData<String> = _thirdAnswerImage
    val fourthAnswerImage: LiveData<String> = _fourthAnswerImage

    fun setQuestionAnswerImage(value: String, type: Int) {
        when (type) {
            0 -> _questionBodyImage.postValue(value)
            1 -> _firstAnswerImage.postValue(value)
            2 -> _secondAnswerImage.postValue(value)
            3 -> _thirdAnswerImage.postValue(value)
            4 -> _fourthAnswerImage.postValue(value)
        }
    }

    init {
        setMCQVisibility(View.GONE)
        setEssayVisibility(View.GONE)
        _essayAnswerText.postValue(false)
        _essayAnswerImage.postValue(false)
    }

    fun setMCQVisibility(value: Int) {
        _mcqAnswerVisibility.postValue(value)
    }

    fun setEssayVisibility(value: Int) {
        _essayAnswerVisibility.postValue(value)
    }

    fun resetResult() {
        _result.postValue(String())
    }

    fun setQuestionType(value: String) {
        _questionType.postValue(value)
    }

    fun setEssayAnswer(answer: Boolean, type: Int) {
        when (type) {
            0 -> _essayAnswerText.postValue(answer)
            1 -> _essayAnswerImage.postValue(answer)
        }
    }

    fun checkDataValidation(): String {
        if (questionHeader.value.isNullOrBlank())
            return EMPTY_QUESTION_HEADER
        if (questionBodyText.value.isNullOrBlank() && questionBodyImage.value.isNullOrBlank())
            return EMPTY_QUESTION_BODY
        if (questionType.value.isNullOrBlank())
            return EMPTY_QUESTION_TYPE

        if (questionType.value == "Essay"
            && (!essayAnswerText.isInitialized || essayAnswerText.value == false)
            && (!essayAnswerImage.isInitialized || essayAnswerImage.value == false)
        )
            return EMPTY_ESSAY_ANSWER
        if (questionType.value == "MCQ"
            && firstAnswerText.value.isNullOrBlank() && firstAnswerImage.value.isNullOrBlank()
        ) return EMPTY_MCQ_FIRST_CHOICE
        if (questionType.value == "MCQ"
            && secondAnswerText.value.isNullOrBlank() && secondAnswerImage.value.isNullOrBlank()
        ) return EMPTY_MCQ_SECOND_CHOICE
        if (questionType.value == "MCQ"
            && thirdAnswerText.value.isNullOrBlank() && thirdAnswerImage.value.isNullOrBlank()
        ) return EMPTY_MCQ_THIRD_CHOICE
        if (questionType.value == "MCQ"
            && fourthAnswerText.value.isNullOrBlank() && fourthAnswerImage.value.isNullOrBlank()
        ) return EMPTY_MCQ_FOURTH_CHOICE
        if (questionScore.value.isNullOrBlank())
            return EMPTY_QUESTION_SCORE
        if (questionScore.value!!.trim().toInt() <= 0)
            return INVALID_QUESTION_SCORE
        return VALID_DATA
    }

    private fun toastRested() {
        showToast("Image Rest")
    }

    fun resetQuestionBodyImage() {
        _questionBodyImage.postValue(String())
        toastRested()
    }

    fun resetFirstAnswerBodyImage() {
        _firstAnswerImage.postValue(String())
        toastRested()

    }

    fun resetSecondAnswerImage() {
        _secondAnswerImage.postValue(String())
        toastRested()

    }

    fun resetThirdAnswerImage() {
        _thirdAnswerImage.postValue(String())
        toastRested()

    }

    fun resetFourthAnswerImage() {
        _fourthAnswerImage.postValue(String())
        toastRested()
    }

    suspend fun saveQuestionToDatabase() {
        viewModelScope.launch {
            positiveShowLoading()
            val response = checkDataValidation()
            if (response == VALID_DATA) {
                val question = getQuestion()
                insertQuestion(
                    question
                )
                _result.postValue(RESPONSE_SUCCESS)
            } else _result.postValue(response)
        }
    }

    fun getQuestion(): Question {
        return Question(
            quizId = quizId,
            number = questionNumber,
            headerText = questionHeader.value!!.m391Capitalize(),
            bodyText = questionBodyText.value?.m391Capitalize(),
            bodyImage = questionBodyImage.value.m391ByteArray(app.contentResolver),
            answerType = questionType.value!!,
            answerBodyText = essayAnswerText.value!!,
            answerBodyImage = essayAnswerImage.value!!,
            questionScore = questionScore.value!!.trim().toInt(),
            answerChoicesFirstText = firstAnswerText.value?.m391Capitalize(),
            answerChoicesFirstImage = firstAnswerImage.value.m391ByteArray(app.contentResolver),
            answerChoicesSecondText = secondAnswerText.value?.m391Capitalize(),
            answerChoicesSecondImage = secondAnswerImage.value.m391ByteArray(app.contentResolver),
            answerChoicesThirdText = thirdAnswerText.value?.m391Capitalize(),
            answerChoicesThirdImage = thirdAnswerImage.value.m391ByteArray(app.contentResolver),
            answerChoicesFourthText = fourthAnswerText.value?.m391Capitalize(),
            answerChoicesFourthImage = fourthAnswerImage.value.m391ByteArray(app.contentResolver)
        )
    }
}