package com.m391.quiz.ui.student

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.m391.quiz.R
import com.m391.quiz.databinding.ActivityStudentBinding

class StudentActivity : AppCompatActivity() {
    private val binding: ActivityStudentBinding by lazy {
        ActivityStudentBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}