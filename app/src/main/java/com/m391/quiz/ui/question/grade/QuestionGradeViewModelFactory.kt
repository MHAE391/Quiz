package com.m391.quiz.ui.question.grade

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.m391.quiz.database.remote.Solutions
import com.m391.quiz.models.QuestionFirebaseUIModel
import com.m391.quiz.ui.quiz.marking.mark.MarkQuizViewModel

class QuestionGradeViewModelFactory(
    private val app: Application,
    private val question: QuestionFirebaseUIModel,
    private val solution: Solutions,
    private val studentId: String
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuestionGradeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuestionGradeViewModel(app, question, solution, studentId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}