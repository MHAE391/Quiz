package com.m391.quiz.ui.authentication

import android.app.Activity
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.m391.quiz.database.remote.Authentication

class AuthenticationViewModelFactory(
    private val app: Application,
    private val authentication: Authentication
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthenticationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthenticationViewModel(app, authentication) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}