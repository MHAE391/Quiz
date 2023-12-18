package com.m391.quiz.ui.student.teacher

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.m391.quiz.database.remote.Quizzes
import com.m391.quiz.models.QuizFirebaseModel
import com.m391.quiz.ui.shared.BaseViewModel
import kotlinx.coroutines.launch

class TeacherQuizzesViewModel(
    private val app: Application,
    private val firebaseQuizzes: Quizzes,
    private val teacherId: String
) : BaseViewModel(app) {
    private val _quizzes = MutableLiveData<List<QuizFirebaseModel>>()
    val quizzes: LiveData<List<QuizFirebaseModel>> = _quizzes

    suspend fun refreshQuizzes(lifecycleOwner: LifecycleOwner) {
        viewModelScope.launch {
            firebaseQuizzes.getAllQuizzes().observe(lifecycleOwner) { allQuizzes ->
                if (!allQuizzes.isNullOrEmpty()) {
                    _quizzes.postValue(
                        allQuizzes.filter {
                            it.quiz_creator == teacherId
                        }
                    )
                }
            }
        }
    }

    suspend fun stopRefreshQuizzes(lifecycleOwner: LifecycleOwner) {
        viewModelScope.launch {
            firebaseQuizzes.getAllQuizzes().removeObservers(lifecycleOwner)
            firebaseQuizzes.closeQuizzesStream()
        }
    }
}