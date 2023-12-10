package com.m391.quiz.ui.teacher.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.m391.quiz.database.local.entities.Quiz
import com.m391.quiz.database.local.interfaces.QuizInterface
import com.m391.quiz.database.local.repositories.QuizRepository
import com.m391.quiz.ui.shared.BaseViewModel
import kotlinx.coroutines.launch

class TeacherHomeViewModel(
    private val app: Application,
    private val getAllQuizzes: suspend () -> List<Quiz>
) : BaseViewModel(app) {
    private val _unCompletedQuizzes = MutableLiveData<List<Quiz>>()
    val unCompletedQuizzes: LiveData<List<Quiz>> = _unCompletedQuizzes


    suspend fun refreshUnCompletedQuizzes() {
        _unCompletedQuizzes.postValue(
            getAllQuizzes()
        )
    }
}