package com.m391.quiz.ui.quiz.solve

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.m391.quiz.database.remote.Quizzes
import com.m391.quiz.models.QuestionFirebaseModel
import com.m391.quiz.models.QuizFirebaseModel

class SolveQuizViewModelFactory(
    private val app: Application,
    private val quiz: QuizFirebaseModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SolveQuizViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SolveQuizViewModel(
                app = app,
                quiz
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}