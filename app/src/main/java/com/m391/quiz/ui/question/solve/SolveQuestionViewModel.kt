package com.m391.quiz.ui.question.solve

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.m391.quiz.R
import com.m391.quiz.database.remote.Solutions
import com.m391.quiz.models.QuestionFirebaseUIModel
import com.m391.quiz.models.SolutionFirebaseModel
import com.m391.quiz.ui.shared.BaseViewModel
import com.m391.quiz.utils.Statics.ESSAY_ANSWER_ERROR
import com.m391.quiz.utils.Statics.MCQ_ANSWER_ERROR
import com.m391.quiz.utils.Statics.QUIZ_TIME_OUT
import com.m391.quiz.utils.Statics.RESPONSE_SUCCESS
import com.m391.quiz.utils.Statics.SOLUTION_VALIDATION_SUCCESS
import com.m391.quiz.utils.m391FirebaseModel
import kotlinx.coroutines.launch

class SolveQuestionViewModel(
    private val app: Application,
    val question: QuestionFirebaseUIModel,
    private val solutions: Solutions,
    quizProgress: Long,
    private val quizDuration: Long
) : BaseViewModel(app), LifecycleObserver {
    val answerTextBody = MutableLiveData<String?>()
    private val _answerImageBody = MutableLiveData<String?>()
    private val _mcqAnswer = MutableLiveData<String?>()
    private val _quizProgress = MutableLiveData<Long>()
    val quizProgress: LiveData<Long> = _quizProgress

    private val handler = Handler(Looper.myLooper()!!)
    private var progress = quizProgress
    private val runnable = object : Runnable {
        override fun run() {
            progress += 10
            _quizProgress.postValue(progress)
            handler.postDelayed(this, 10)
        }
    }

    fun stopProgress() {
        handler.removeCallbacks(runnable)
    }

    init {
        handler.postDelayed(runnable, 10)
    }

    private val _response = MutableLiveData<String>()
    val response: LiveData<String> = _response
    fun setAnswerImageBody(answer: String?, message: String) {
        _answerImageBody.postValue(answer)
        showToast(message)
    }

    fun setMCQAnswer(answer: String) {
        _mcqAnswer.postValue(answer)
    }

    suspend fun uploadQuestionSolution() {
        viewModelScope.launch {
            positiveShowLoading()
            val result = checkAnswerValidation()
            if (result == SOLUTION_VALIDATION_SUCCESS) {
                _response.postValue(
                    solutions.uploadQuestionAnswer(
                        getSolutionObject()
                    )
                )
            } else _response.postValue(result)
        }
    }

    private fun checkAnswerValidation(): String {
        var response = SOLUTION_VALIDATION_SUCCESS
        if (_quizProgress.value!! >= quizDuration) response = QUIZ_TIME_OUT
        if (question.answerType == "MCQ") {
            if (_mcqAnswer.value.isNullOrBlank()) response = MCQ_ANSWER_ERROR
        } else {
            if (_answerImageBody.value.isNullOrBlank() && answerTextBody.value.isNullOrBlank())
                response = ESSAY_ANSWER_ERROR
        }
        return response
    }

    private fun getSolutionObject() = SolutionFirebaseModel(
        question = question.m391FirebaseModel(),
        textAnswer = answerTextBody.value,
        imageAnswer = _answerImageBody.value,
        mcqAnswer = _mcqAnswer.value
    )

    fun resetResponse() {
        _response.postValue(String())
    }
}