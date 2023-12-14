package com.m391.quiz.ui.question.preview

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.m391.quiz.database.local.entities.Question
import com.m391.quiz.ui.shared.BaseViewModel
import kotlinx.coroutines.launch

class PreviewQuestionViewModel(
    private val app: Application,
    val question: Question,
    private val deleteQuestion: suspend (Question) -> Unit
) : BaseViewModel(app) {
    suspend fun deleteQuestion() {
        viewModelScope.launch {
            deleteQuestion(question)
        }
    }
}