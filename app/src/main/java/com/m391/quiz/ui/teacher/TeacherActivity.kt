package com.m391.quiz.ui.teacher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.m391.quiz.R
import com.m391.quiz.database.local.repositories.QuizRepository
import com.m391.quiz.databinding.ActivityTeacherBinding

class TeacherActivity : AppCompatActivity() {
    private val binding: ActivityTeacherBinding by lazy {
        ActivityTeacherBinding.inflate(layoutInflater)
    }

    private val teacherViewModel: TeacherViewModel by viewModels {
        TeacherViewModelFactory(application, QuizRepository(application.applicationContext))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}