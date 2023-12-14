package com.m391.quiz.ui.teacher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.m391.quiz.database.local.repositories.QuizRepository
import com.m391.quiz.databinding.ActivityTeacherBinding
import com.m391.quiz.ui.quiz.QuizViewModel
import com.m391.quiz.ui.quiz.QuizViewModelFactory

class TeacherActivity : AppCompatActivity() {
    private val binding: ActivityTeacherBinding by lazy {
        ActivityTeacherBinding.inflate(layoutInflater)
    }

    private val teacherViewModel: QuizViewModel by viewModels {
        QuizViewModelFactory(application, QuizRepository(application.applicationContext))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}