package com.m391.quiz.ui.authentication.information.student

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.m391.quiz.ui.authentication.information.teacher.TeacherInformationViewModel

class StudentInformationViewModelFactory(
    private val app: Application,
    private val setUserData: suspend (
        userType: String,
        profileImage: String,
        firstName: String,
        lastName: String,
        subjects: List<String>,
        academicYears: List<String>,
        dateOfBarth: String
    ) -> String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentInformationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StudentInformationViewModel(app, setUserData) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}