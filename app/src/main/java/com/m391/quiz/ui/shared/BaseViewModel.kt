package com.m391.quiz.ui.shared

import android.app.Application
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import com.m391.quiz.database.remote.RemoteDatabase

open class BaseViewModel(app: Application) : AndroidViewModel(app) {
    val showLoading = MutableLiveData<Boolean>()
    val showNoData = MutableLiveData<Boolean>()

    val showToast: (String) -> Unit =
        { message ->
            Toast.makeText(app, message, Toast.LENGTH_SHORT).show()
        }
    val showSnackBar: (String, View) -> Unit =
        { message, view ->
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
        }

    fun positiveShowLoading() {
        showLoading.postValue(true)
    }

    fun negativeShowLoading() {
        showLoading.postValue(false)
    }

    fun positiveShowNoData() {
        showNoData.postValue(true)
    }

    fun negativeShowNoData() {
        showNoData.postValue(false)
    }
}