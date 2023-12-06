package com.m391.quiz.ui.authentication.information.shared

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.m391.quiz.R
import com.m391.quiz.databinding.FragmentAcademicSubjectsBinding


class AcademicSubjectsFragment : Fragment() {

    private val binding: FragmentAcademicSubjectsBinding by lazy {
        FragmentAcademicSubjectsBinding.inflate(layoutInflater)
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