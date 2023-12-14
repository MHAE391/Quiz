package com.m391.quiz.ui.quiz

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.m391.quiz.database.local.interfaces.QuizInterface

class QuizViewModelFactory(
    private val app: Application,
    private val quizInterface: QuizInterface
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuizViewModel(app, quizInterface) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}