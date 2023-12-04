package com.m391.quiz.ui.authentication.phone

import android.app.Activity
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.m391.quiz.database.remote.Authentication

class PhoneNumberViewModelFactory(
    private val app: Application,
    private val response: LiveData<String>,
    private val getOTPCode: suspend (String?, Activity) -> Unit
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PhoneNumberViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PhoneNumberViewModel(app, response, getOTPCode) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}