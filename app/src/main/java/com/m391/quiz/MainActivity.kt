package com.m391.quiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.m391.quiz.database.remote.RemoteDatabase
import com.m391.quiz.databinding.ActivityMainBinding
import com.m391.quiz.ui.authentication.AuthenticationActivity
import com.m391.quiz.ui.authentication.information.InformationActivity
import com.m391.quiz.ui.student.StudentActivity
import com.m391.quiz.ui.teacher.TeacherActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val remoteDatabase by lazy {
        RemoteDatabase()
    }
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (remoteDatabase.authentication.getCurrentUser() == null) {
            startActivity(Intent(this@MainActivity, AuthenticationActivity::class.java))
            finish()
        } else {
            lifecycleScope.launch {
                if (remoteDatabase.information.checkAlreadyTeacherOrNot()) {
                    startActivity(Intent(this@MainActivity, TeacherActivity::class.java))
                    finish()
                } else if (remoteDatabase.information.checkAlreadyStudentOrNot()) {
                    startActivity(Intent(this@MainActivity, StudentActivity::class.java))
                    finish()
                } else {
                    startActivity(Intent(this@MainActivity, InformationActivity::class.java))
                    finish()
                }
            }
        }
    }
}