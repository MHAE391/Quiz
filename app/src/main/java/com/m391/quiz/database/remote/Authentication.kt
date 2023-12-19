package com.m391.quiz.database.remote

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.m391.quiz.utils.Statics.INVALID_CODE
import com.m391.quiz.utils.Statics.SUCCESSFUL_LOGIN
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class Authentication {
    private val auth = FirebaseAuth.getInstance()
    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    suspend fun sendVerificationCodeAsync(
        phone: String,
        callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks,
        activity: Activity
    ) = withContext(Dispatchers.IO) {
        val options =
            PhoneAuthOptions
                .newBuilder(auth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(callback).build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    suspend fun resendVerificationCode(
        phone: String,
        token: PhoneAuthProvider.ForceResendingToken,
        callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks,
        activity: Activity
    ) = withContext(Dispatchers.IO) {
        val options =
            PhoneAuthOptions
                .newBuilder(auth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setCallbacks(callback)
                .setForceResendingToken(token)
                .setActivity(activity)
                .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    suspend fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential): String? =
        withContext(Dispatchers.IO) {
            var authResponse: String? = null
            try {
                auth.signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) authResponse = SUCCESSFUL_LOGIN
                        else {
                            if (task.exception is FirebaseAuthInvalidCredentialsException) {
                                authResponse = INVALID_CODE
                            }
                        }
                    }.await()

            } catch (e: Exception) {
                authResponse = INVALID_CODE
            }
            return@withContext authResponse
        }
}