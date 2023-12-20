package com.m391.quiz.ui.question.marking

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.util.query
import com.m391.quiz.database.remote.Solutions
import com.m391.quiz.models.QuestionFirebaseUIModel
import com.m391.quiz.models.QuestionScore
import com.m391.quiz.ui.question.preview.PreviewQuestionViewModel

class MarkQuestionViewModelFactory(
    private val app: Application,
    private val question: QuestionFirebaseUIModel,
    private val solutions: Solutions,
    private val studentUid: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MarkQuestionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MarkQuestionViewModel(
                app = app,
                questionData = question,
                solutions = solutions,
                studentUid
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}