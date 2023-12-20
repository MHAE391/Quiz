package com.m391.quiz.ui.quiz.solve

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.m391.quiz.models.QuestionFirebaseUIModel
import com.m391.quiz.models.QuizFirebaseModel
import com.m391.quiz.ui.shared.BaseViewModel
import com.m391.quiz.utils.m391FirebaseUIModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.m391.quiz.database.remote.Solutions
import com.m391.quiz.models.QuestionScore
import com.m391.quiz.utils.Statics.SOLVER_SUCCESS_RESPONSE
import kotlinx.coroutines.launch

class SolveQuizViewModel(
    private val app: Application,
    val quiz: QuizFirebaseModel,
    private val solutions: Solutions,
    private val studentUid: String
) : BaseViewModel(app), LifecycleObserver {

    private val _questions = MutableLiveData<List<QuestionFirebaseUIModel>>()
    val questions: LiveData<List<QuestionFirebaseUIModel>> = _questions
    private val _quizProgress = MutableLiveData<Long>()
    val quizProgress: LiveData<Long> = _quizProgress
    private val handler = Handler(Looper.myLooper()!!)
    private var progress = 0L
    private val runnable = object : Runnable {
        override fun run() {
            progress += 10
            _quizProgress.postValue(progress)
            handler.postDelayed(this, 10)
        }
    }
    private val _response = MutableLiveData<String>()
    val response: LiveData<String> = _response

    private val _quizStarted = MutableLiveData<Boolean>()
    val quizStarted: LiveData<Boolean> = _quizStarted


    fun refreshQuestions() {
        _questions.postValue(
            quiz.questions.m391FirebaseUIModel()
        )
    }

    fun resetResponse() {
        _response.postValue(String())
    }

    fun startProgress() {
        handler.postDelayed(runnable, 10)
        _quizStarted.postValue(true)
        showToast(SOLVER_SUCCESS_RESPONSE)
    }

    fun startQuiz() {
        positiveShowLoading()
        viewModelScope.launch {
            _response.postValue(solutions.startQuiz(quiz))
        }
    }

    private val _studentTotalScore = MutableLiveData<Int>()

    val studentTotalScore: LiveData<Int> = _studentTotalScore

    suspend fun getStudentScores(lifecycleOwner: LifecycleOwner) {
        viewModelScope.launch {
            solutions.getStudentQuizScores(studentUid, quiz.quiz_id).observe(lifecycleOwner) {
                _studentTotalScore.postValue(it.m391Score())
            }
        }
    }

    suspend fun removeStudentScores(lifecycleOwner: LifecycleOwner) {
        viewModelScope.launch {
            solutions.getStudentQuizScores(studentUid, quiz.quiz_id).removeObservers(lifecycleOwner)
            solutions.closeSolutionsStream()
        }
    }

    private fun List<QuestionScore>.m391Score(): Int {
        var score = 0
        this.forEach {
            score += it.score ?: 0
        }
        return score
    }

}