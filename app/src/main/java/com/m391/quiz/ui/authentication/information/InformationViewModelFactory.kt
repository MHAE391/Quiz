package com.m391.quiz.ui.authentication.information

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.m391.quiz.database.remote.Information
import com.m391.quiz.ui.authentication.AuthenticationViewModel

class InformationViewModelFactory(
    private val app: Application, private val information: Information
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InformationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InformationViewModel(app, information) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}