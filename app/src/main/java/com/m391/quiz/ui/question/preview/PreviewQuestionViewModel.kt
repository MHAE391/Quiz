package com.m391.quiz.ui.question.preview

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.m391.quiz.database.local.entities.Question
import com.m391.quiz.models.QuestionUIModel
import com.m391.quiz.ui.shared.BaseViewModel
import com.m391.quiz.utils.m391DatabaseModel
import kotlinx.coroutines.launch

class PreviewQuestionViewModel(
    private val app: Application,
    val questionUIModel: QuestionUIModel,
    private val deleteQuestion: suspend (Question) -> Unit
) : BaseViewModel(app) {
    suspend fun deleteQuestion() {
        viewModelScope.launch {
            deleteQuestion(questionUIModel.m391DatabaseModel())
        }
    }
}