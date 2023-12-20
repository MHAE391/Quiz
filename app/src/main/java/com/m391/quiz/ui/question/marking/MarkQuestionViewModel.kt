package com.m391.quiz.ui.question.marking

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.m391.quiz.database.remote.Solutions
import com.m391.quiz.models.QuestionFirebaseUIModel
import com.m391.quiz.models.QuestionScore
import com.m391.quiz.models.SolutionFirebaseModel
import com.m391.quiz.ui.shared.BaseViewModel
import com.m391.quiz.utils.Statics.MCQ_FIRST
import com.m391.quiz.utils.Statics.MCQ_FOURTH
import com.m391.quiz.utils.Statics.MCQ_SECOND
import com.m391.quiz.utils.Statics.MCQ_THIRD
import com.m391.quiz.utils.Statics.RESPONSE_SUCCESS
import com.m391.quiz.utils.Statics.SCORE_EMPTY_RESPONSE_ERROR
import com.m391.quiz.utils.Statics.SCORE_VALUE_RESPONSE_ERROR
import kotlinx.coroutines.launch

class MarkQuestionViewModel(
    private val app: Application,
    private val questionData: QuestionFirebaseUIModel,
    private val solutions: Solutions,
    private val studentUid: String
) : BaseViewModel(app) {
    private val _question = MutableLiveData<QuestionFirebaseUIModel>()
    val question: LiveData<QuestionFirebaseUIModel> = _question

    private val _studentSolution = MutableLiveData<SolutionFirebaseModel>()
    val studentSolution: LiveData<SolutionFirebaseModel> = _studentSolution

    private val _questionScore = MutableLiveData<QuestionScore?>()
    val questionScore: LiveData<QuestionScore?> = _questionScore


    val firstMCQChecked = MutableLiveData<Boolean>()
    val secondMCQChecked = MutableLiveData<Boolean>()
    val thirdMCQChecked = MutableLiveData<Boolean>()
    val fourthMCQChecked = MutableLiveData<Boolean>()

    val answerScore = MutableLiveData<String>()
    val answerComment = MutableLiveData<String>()

    suspend fun studentSolution(lifecycleOwner: LifecycleOwner) {
        viewModelScope.launch {
            solutions.getStudentSolution(studentUid, questionData.questionId, questionData.quizId)
                .observe(
                    lifecycleOwner
                ) {
                    _studentSolution.postValue(it)
                    if (questionData.answerType == "MCQ") {
                        when (it.mcqAnswer) {
                            MCQ_FIRST -> firstMCQChecked.postValue(true)
                            MCQ_SECOND -> secondMCQChecked.postValue(true)
                            MCQ_THIRD -> thirdMCQChecked.postValue(true)
                            MCQ_FOURTH -> fourthMCQChecked.postValue(true)
                        }
                    }
                }
        }
    }

    suspend fun getQuestionScore(lifecycleOwner: LifecycleOwner) {
        viewModelScope.launch {
            solutions.getStudentQuestionScore(
                studentUid,
                questionData.quizId,
                questionData.questionId
            ).observe(lifecycleOwner) {
                _questionScore.postValue(it)
            }
        }
    }

    init {
        _question.postValue(questionData)
    }

    suspend fun stopStudentSolution(lifecycleOwner: LifecycleOwner) {
        viewModelScope.launch {
            solutions.getStudentSolution(studentUid, questionData.questionId, questionData.quizId)
                .removeObservers(lifecycleOwner)
            solutions.closeSolutionsStream()
        }
    }

    private val _response = MutableLiveData<String>()
    val response: LiveData<String> = _response
    fun resetResponse() {
        _response.postValue(String())
    }

    suspend fun uploadScore() {
        positiveShowLoading()
        viewModelScope.launch {
            val result = checkScoreValidation()
            if (result == RESPONSE_SUCCESS)
                _response.postValue(
                    solutions.uploadQuestionScore(
                        studentUid, questionData.questionId, questionData.quizId,
                        answerScore.value!!.toInt(),
                        answerComment.value?.trim()
                    )
                )
            else _response.postValue(result)
        }
    }

    private fun checkScoreValidation(): String {
        var response = RESPONSE_SUCCESS
        if (answerScore.value.isNullOrBlank()) response = SCORE_EMPTY_RESPONSE_ERROR
        else if (answerScore.value!!.toInt() > questionData.questionScore) response =
            SCORE_VALUE_RESPONSE_ERROR
        return response
    }
}