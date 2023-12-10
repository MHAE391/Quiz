package com.m391.quiz.ui.teacher

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.m391.quiz.database.local.interfaces.QuizInterface
import com.m391.quiz.ui.authentication.phone.PhoneNumberViewModel

class TeacherViewModelFactory(
    private val app: Application,
    private val quizInterface: QuizInterface
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TeacherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TeacherViewModel(app, quizInterface) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}