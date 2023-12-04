package com.m391.quiz.ui.authentication.otp

import android.app.Activity
import android.app.Application
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.m391.quiz.ui.shared.BaseViewModel
import com.m391.quiz.utils.Statics.NOT_COMPLETE_OTP
import kotlinx.coroutines.launch

class OTPVerificationViewModel(
    private val app: Application,
    val response: LiveData<String>,
    val phoneNumber: String,
    private val resendCode: suspend (String, Activity) -> Unit,
    private val verifyCode: suspend (String) -> Unit
) : BaseViewModel(app) {
    val firstCode = MutableLiveData<String>()
    val secondCode = MutableLiveData<String>()
    val thirdCode = MutableLiveData<String>()
    val fourthCode = MutableLiveData<String>()
    val fifthCode = MutableLiveData<String>()
    val sixthCode = MutableLiveData<String>()

    suspend fun verify(restEditTextCodes: (Boolean) -> Unit) {
        if (validateOTP()) {
            showToast(NOT_COMPLETE_OTP)
            resetData()
        } else {
            positiveShowLoading()
            verifyCode(getOTPCode())
            restEditTextCodes(false)
        }
    }

    suspend fun sendCodeAgain(activity: Activity) {
        positiveShowLoading()
        resendCode(phoneNumber, activity)
    }

    private fun validateOTP(): Boolean {
        return firstCode.value.isNullOrEmpty() ||
                secondCode.value.isNullOrEmpty() ||
                thirdCode.value.isNullOrEmpty() ||
                fourthCode.value.isNullOrEmpty() ||
                fifthCode.value.isNullOrEmpty() ||
                sixthCode.value.isNullOrEmpty()
    }

    private fun getOTPCode() =
        firstCode.value + secondCode.value + thirdCode.value + fourthCode.value + fifthCode.value + sixthCode.value

    fun resetData() {
        firstCode.value = String()
        secondCode.value = String()
        thirdCode.value = String()
        fourthCode.value = String()
        fifthCode.value = String()
        sixthCode.value = String()
    }

    fun setTextWatcher(view: EditText, secondView: EditText) {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                view.requestFocus()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                if (!view.text.isNullOrEmpty()) secondView.requestFocus()
                else view.requestFocus()
            }
        }
        view.addTextChangedListener(textWatcher)
    }

    fun setLastTextWatcher(view: EditText, button: Button, restEditTextCodes: (Boolean) -> Unit) {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                if (!view.text.isNullOrEmpty()) {
                    button.isEnabled = false
                    viewModelScope.launch {
                        verify(restEditTextCodes)
                    }
                }
            }
        }
        view.addTextChangedListener(textWatcher)
    }
}