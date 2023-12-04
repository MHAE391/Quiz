package com.m391.quiz.ui.authentication.phone

import android.app.Activity
import android.app.Application
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.m391.quiz.database.remote.Authentication
import com.m391.quiz.ui.shared.BaseViewModel
import com.m391.quiz.utils.Statics
import com.m391.quiz.utils.Statics.EMPTY_PHONE_NUMBER
import com.m391.quiz.utils.Statics.INVALID_PHONE_NUMBER
import com.m391.quiz.utils.Statics.RESPONSE_INVALID_REQUEST
import com.m391.quiz.utils.Statics.RESPONSE_RECAPTCHA_WITH_NULL_ACTIVITY
import com.m391.quiz.utils.Statics.RESPONSE_SMS_LIMIT_EXCEEDED
import com.m391.quiz.utils.Statics.RESPONSE_SUCCESS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.regex.Matcher
import java.util.regex.Pattern

class PhoneNumberViewModel(
    app: Application,
    val response: LiveData<String>,
    private val getOTPCode: suspend (String?, Activity) -> Unit
) :
    BaseViewModel(app) {
    val phoneNumber = MutableLiveData<String>()

    suspend fun getCode(activity: Activity, button: Button, editText: EditText) {
        positiveShowLoading()
        if (phoneNumber.value.isNullOrBlank()) {
            showErrorMessage(button, EMPTY_PHONE_NUMBER)
        } else if (!isValidPhone(phoneNumber.value!!.trim())) {
            showErrorMessage(button, INVALID_PHONE_NUMBER)
        } else {
            button.isEnabled = false
            editText.isEnabled = false
            getOTPCode(phoneNumber.value, activity)
        }
    }

    private fun showErrorMessage(view: View, message: String) {
        negativeShowLoading()
        showSnackBar(message, view)
    }

    private fun isValidPhone(phone: String): Boolean {
        val expression = "^01[0-2]\\d{1,8}\$"
        val inputString: CharSequence = phone
        val pattern: Pattern = Pattern.compile(expression)
        val matcher: Matcher = pattern.matcher(inputString)
        return matcher.matches() and (phone.length == 11)
    }
}