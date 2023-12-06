package com.m391.quiz.ui.authentication.information

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.navigation.findNavController
import com.m391.quiz.R
import com.m391.quiz.databinding.ActivityInformationBinding
import com.m391.quiz.ui.authentication.information.student.StudentInformationFragment
import com.m391.quiz.ui.authentication.information.student.StudentInformationFragmentDirections
import com.m391.quiz.ui.authentication.information.teacher.TeacherInformationFragment
import com.m391.quiz.ui.authentication.information.teacher.TeacherInformationFragmentDirections

class InformationActivity : AppCompatActivity() {
    private val binding: ActivityInformationBinding by lazy {
        ActivityInformationBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }

    override fun onStart() {
        super.onStart()

        binding.switchButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.navHostFragment.findNavController().navigate(
                    StudentInformationFragmentDirections.actionStudentInformationFragmentToTeacherInformationFragment()
                )
            } else {
                binding.navHostFragment.findNavController().navigate(
                    TeacherInformationFragmentDirections.actionTeacherInformationFragmentToStudentInformationFragment()
                )
            }
        }
    }


}