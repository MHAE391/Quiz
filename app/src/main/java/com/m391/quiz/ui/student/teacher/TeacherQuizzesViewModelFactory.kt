package com.m391.quiz.ui.student.teacher

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.m391.quiz.database.remote.Quizzes
import com.m391.quiz.ui.student.home.StudentHomeViewModel

class TeacherQuizzesViewModelFactory(
    private val app: Application,
    private val firebaseQuizzes: Quizzes,
    private val teacherId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TeacherQuizzesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TeacherQuizzesViewModel(
                app,
                firebaseQuizzes,
                teacherId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}