package com.m391.quiz.ui.authentication.information

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.m391.quiz.R
import com.m391.quiz.databinding.ActivityInformationBinding

class InformationActivity : AppCompatActivity() {
    private val binding: ActivityInformationBinding by lazy {
        ActivityInformationBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}