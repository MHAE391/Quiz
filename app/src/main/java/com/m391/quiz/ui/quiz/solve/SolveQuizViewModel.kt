package com.m391.quiz.ui.quiz.solve

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.m391.quiz.database.remote.Quizzes
import com.m391.quiz.models.QuestionFirebaseModel
import com.m391.quiz.models.QuestionFirebaseUIModel
import com.m391.quiz.models.QuizFirebaseModel
import com.m391.quiz.ui.shared.BaseViewModel
import com.m391.quiz.utils.m391FirebaseUIModel
import androidx.lifecycle.LifecycleObserver
import kotlinx.coroutines.launch

class SolveQuizViewModel(
    private val app: Application,
    val quiz: QuizFirebaseModel
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


    fun refreshQuestions() {
        _questions.postValue(
            quiz.questions.m391FirebaseUIModel()
        )
    }

    fun startProgress() {
        handler.postDelayed(runnable, 10)
        showToast("Quiz Started")
    }

}