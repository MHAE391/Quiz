package com.m391.quiz.ui.authentication.information.teacher

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.m391.quiz.R
import com.m391.quiz.databinding.FragmentTeacherInformationBinding

class TeacherInformationFragment : Fragment() {

    private val binding: FragmentTeacherInformationBinding by lazy {
        FragmentTeacherInformationBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }
}