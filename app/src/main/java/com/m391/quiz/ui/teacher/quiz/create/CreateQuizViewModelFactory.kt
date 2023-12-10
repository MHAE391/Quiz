package com.m391.quiz.ui.teacher.quiz.create

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.m391.quiz.database.local.entities.Quiz

class CreateQuizViewModelFactory(
    private val app: Application,
    private val createQuiz: suspend (quiz: Quiz) -> Unit,
    private val currentUserUID: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateQuizViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateQuizViewModel(app, createQuiz, currentUserUID) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}