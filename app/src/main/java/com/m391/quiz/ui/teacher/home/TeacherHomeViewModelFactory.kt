package com.m391.quiz.ui.teacher.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.m391.quiz.database.local.entities.Quiz
import com.m391.quiz.database.local.interfaces.QuizInterface
import com.m391.quiz.database.remote.Quizzes
import com.m391.quiz.ui.authentication.phone.PhoneNumberViewModel

class TeacherHomeViewModelFactory(
    private val app: Application,
    private val getAllQuizzes: suspend () -> List<Quiz>,
    private val quizzes: Quizzes,
    private val uid: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TeacherHomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TeacherHomeViewModel(app, getAllQuizzes, quizzes, uid) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}