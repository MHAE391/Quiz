package com.m391.quiz.ui.student.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.m391.quiz.database.remote.Teachers
import com.m391.quiz.ui.quiz.preview.PreviewQuizViewModel

class StudentHomeViewModelFactory(private val app: Application, private val teachers: Teachers) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentHomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StudentHomeViewModel(
                app,
                teachers
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}