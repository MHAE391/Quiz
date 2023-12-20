package com.m391.quiz.ui.quiz.marking.mark

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.m391.quiz.database.remote.Solutions
import com.m391.quiz.models.QuizFirebaseModel
import com.m391.quiz.ui.quiz.create.CreateQuizViewModel

class MarkQuizViewModelFactory(
    private val app: Application,
    private val quiz: QuizFirebaseModel,
    private val studentUid: String,
    private val solutions: Solutions
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MarkQuizViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MarkQuizViewModel(app, quiz, studentUid, solutions) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}