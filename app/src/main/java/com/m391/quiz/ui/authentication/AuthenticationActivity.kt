package com.m391.quiz.ui.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.m391.quiz.R
import com.m391.quiz.databinding.ActivityAuthenticationBinding

class AuthenticationActivity : AppCompatActivity() {
    private val binding: ActivityAuthenticationBinding by lazy {
        ActivityAuthenticationBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}