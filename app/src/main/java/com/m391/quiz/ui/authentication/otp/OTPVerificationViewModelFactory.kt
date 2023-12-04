package com.m391.quiz.ui.authentication.otp

import android.app.Activity
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class OTPVerificationViewModelFactory(
    private val app: Application,
    private val response: LiveData<String>,
    private val phoneNumber: String,
    private val resendCode: suspend (String, Activity) -> Unit,
    private val verifyCode: suspend (String) -> Unit
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OTPVerificationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OTPVerificationViewModel(
                app,
                response,
                phoneNumber,
                resendCode,
                verifyCode
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}