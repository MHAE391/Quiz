package com.m391.quiz.ui.teacher.home

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.m391.quiz.database.local.entities.Quiz
import com.m391.quiz.database.local.interfaces.QuizInterface
import com.m391.quiz.database.local.repositories.QuizRepository
import com.m391.quiz.database.remote.Quizzes
import com.m391.quiz.models.QuizFirebaseModel
import com.m391.quiz.ui.shared.BaseViewModel
import kotlinx.coroutines.launch

class TeacherHomeViewModel(
    private val app: Application,
    private val getAllQuizzes: suspend () -> List<Quiz>,
    private val quizzes: Quizzes,
    private val uid: String
) : BaseViewModel(app) {
    private val _unCompletedQuizzes = MutableLiveData<List<Quiz>>()
    val unCompletedQuizzes: LiveData<List<Quiz>> = _unCompletedQuizzes

    private val _completedQuizzes = MutableLiveData<List<QuizFirebaseModel>>()
    val completedQuizzes: LiveData<List<QuizFirebaseModel>> = _completedQuizzes
    suspend fun refreshQuizzes(lifecycleOwner: LifecycleOwner) {
        viewModelScope.launch {
            quizzes.getAllQuizzes().observe(lifecycleOwner) { allQuizzes ->
                if (!allQuizzes.isNullOrEmpty()) {
                    _completedQuizzes.postValue(
                        allQuizzes.filter {
                            it.quiz_creator == uid
                        }
                    )
                }
            }
        }
    }

    suspend fun stopRefreshQuizzes(lifecycleOwner: LifecycleOwner) {
        viewModelScope.launch {
            quizzes.getAllQuizzes().removeObservers(lifecycleOwner)
            quizzes.closeQuizzesStream()
        }
    }

    suspend fun refreshUnCompletedQuizzes() {
        _unCompletedQuizzes.postValue(
            getAllQuizzes()
        )
    }
}