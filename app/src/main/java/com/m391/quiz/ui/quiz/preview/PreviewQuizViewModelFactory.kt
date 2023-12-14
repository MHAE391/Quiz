package com.m391.quiz.ui.quiz.preview

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.m391.quiz.database.local.entities.Question
import com.m391.quiz.database.local.entities.Quiz
import com.m391.quiz.ui.quiz.QuizViewModel

class PreviewQuizViewModelFactory(
    private val app: Application,
    private val quizId: Int,
    private val getQuizById: suspend (quizId: Int) -> Quiz?,
    private val getAllQuizQuestions: suspend (quizId: Int) -> List<Question>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PreviewQuizViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PreviewQuizViewModel(app, quizId, getQuizById, getAllQuizQuestions) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}