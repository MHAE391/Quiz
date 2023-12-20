package com.m391.quiz.ui.student.grades

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.m391.quiz.database.remote.Authentication
import com.m391.quiz.database.remote.Quizzes
import com.m391.quiz.database.remote.Solutions
import com.m391.quiz.ui.student.home.StudentHomeViewModel

class QuizzesAnswersViewModelFactory(
    private val app: Application,
    private val quizzes: Quizzes,
    private val solutions: Solutions,
    private val user: String
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizzesAnswersViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuizzesAnswersViewModel(
                app,
                quizzes,
                solutions,
                user
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}