package com.m391.quiz.ui.question.create

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.m391.quiz.database.local.entities.Question
import com.m391.quiz.ui.quiz.create.CreateQuizViewModel

class CreateQuestionViewModelFactory(
    private val app: Application,
    private val quizId: Int,
    private val questionNumber: Int,
    private val insertQuestion: suspend (Question) -> Unit
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateQuestionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateQuestionViewModel(app, quizId, questionNumber ,insertQuestion) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}