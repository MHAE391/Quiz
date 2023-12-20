package com.m391.quiz.ui.student.grades

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.m391.quiz.database.remote.Authentication
import com.m391.quiz.database.remote.Quizzes
import com.m391.quiz.database.remote.Solutions
import com.m391.quiz.models.QuizFirebaseModel
import com.m391.quiz.ui.shared.BaseViewModel
import com.m391.quiz.utils.Statics
import com.m391.quiz.utils.Statics.SOLVER_SUCCESS_RESPONSE
import kotlinx.coroutines.launch

class QuizzesAnswersViewModel(
    private val app: Application,
    private val remoteQuizzes: Quizzes,
    private val solutions: Solutions,
    private val userId: String
) : BaseViewModel(app) {
    private val _quizzes = MutableLiveData<List<QuizFirebaseModel>>()
    val quizzes: LiveData<List<QuizFirebaseModel>> = _quizzes

    suspend fun refreshQuizzes(lifecycleOwner: LifecycleOwner) {
        viewModelScope.launch {
            remoteQuizzes.studentSolvedQuizzes(userId).observe(lifecycleOwner) {
                if (it.isNullOrEmpty()) positiveShowNoData()
                else {
                    negativeShowNoData()
                    _quizzes.postValue(it)
                }

            }
        }
    }

    suspend fun stopRefreshQuizzes(lifecycleOwner: LifecycleOwner) {
        viewModelScope.launch {
            remoteQuizzes.studentSolvedQuizzes(userId).removeObservers(lifecycleOwner)
            remoteQuizzes.closeQuizzesStream()
        }
    }
}