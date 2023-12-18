package com.m391.quiz.ui.question.solve

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.m391.quiz.models.QuestionFirebaseUIModel
import com.m391.quiz.ui.question.preview.PreviewQuestionViewModel

class SolveQuestionViewModelFactory(
    private val app: Application,
    private val question: QuestionFirebaseUIModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SolveQuestionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SolveQuestionViewModel(
                app = app,
                question = question
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}