package com.m391.quiz.ui.authentication

import android.app.Activity
import android.app.Application
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
import com.m391.quiz.utils.Statics.EMPTY_PHONE_NUMBER
import com.m391.quiz.utils.Statics.INVALID_PHONE_NUMBER
import com.m391.quiz.utils.Statics.RESPONSE_INVALID_REQUEST
import com.m391.quiz.utils.Statics.RESPONSE_RECAPTCHA_WITH_NULL_ACTIVITY
import com.m391.quiz.utils.Statics.RESPONSE_SMS_LIMIT_EXCEEDED
import com.m391.quiz.utils.Statics.RESPONSE_SUCCESS
import java.util.regex.Matcher
import java.util.regex.Pattern

class AuthenticationViewModel(
    private val app: Application,
    private val authentication: Authentication
) :
    BaseViewModel(app) {
    private val _response = MutableLiveData<String>()
    val response: LiveData<String> = _response

    private val _storedVerificationId = MutableLiveData<String>()

    init {
        authenticationInit()
    }

    private val _resendToken = MutableLiveData<PhoneAuthProvider.ForceResendingToken>()
    val resendToken: LiveData<PhoneAuthProvider.ForceResendingToken> = _resendToken

    val getOTPCode: suspend (String?, Activity) -> Unit =
        { phoneNumber, activity ->
            authentication.sendVerificationCodeAsync("+2$phoneNumber", otpCallback, activity)
        }
    val resendCode: suspend (String, Activity) -> Unit = { phoneNumber, activity ->
        authentication.resendVerificationCode(
            "+2$phoneNumber",
            resendToken.value!!,
            otpCallback,
            activity
        )
    }
    val verifyCode: suspend (String) -> Unit = { codeOTP ->
        val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
            _storedVerificationId.value!!, codeOTP
        )
        _response.postValue(authentication.signInWithPhoneAuthCredential(credential))
    }


    fun authenticationInit() {
        _response.postValue(String())
    }

    private val otpCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            TODO("Not yet implemented")
        }

        override fun onVerificationFailed(e: FirebaseException) {
            when (e) {
                is FirebaseAuthInvalidCredentialsException -> {
                    // Invalid request
                    _response.postValue(RESPONSE_INVALID_REQUEST)

                }

                is FirebaseTooManyRequestsException -> {
                    // The SMS quota for the project has been exceeded
                    _response.postValue(RESPONSE_SMS_LIMIT_EXCEEDED)

                }

                is FirebaseAuthMissingActivityForRecaptchaException -> {
                    // reCAPTCHA verification attempted with null Activity
                    _response.postValue(RESPONSE_RECAPTCHA_WITH_NULL_ACTIVITY)
                }
            }
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            _storedVerificationId.postValue(verificationId)
            _resendToken.postValue(token)
            _response.postValue(RESPONSE_SUCCESS)
        }
    }

}