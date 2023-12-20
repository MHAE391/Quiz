package com.m391.quiz.ui.quiz.marking.students

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.m391.quiz.database.remote.Solutions
import com.m391.quiz.database.remote.Students

class QuizSolversViewModelFactory(
    private val app: Application,
    private val quizId: String,
    private val solutions: Solutions,
    private val students: Students
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizSolversViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuizSolversViewModel(app, quizId, solutions, students) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}