package com.m391.quiz.ui.question.preview

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.m391.quiz.database.local.entities.Question
import com.m391.quiz.models.QuestionUIModel


class PreviewQuestionViewModelFactory(
    private val app: Application,
    private val questionUIModel: QuestionUIModel,
    private val deleteQuestion: suspend (Question) -> Unit
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PreviewQuestionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PreviewQuestionViewModel(
                app = app,
                questionUIModel = questionUIModel,
                deleteQuestion = deleteQuestion
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}