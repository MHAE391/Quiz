package com.m391.quiz.ui.authentication.phone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.m391.quiz.R
import com.m391.quiz.databinding.FragmentPhoneNumberBinding

class PhoneNumberFragment : Fragment() {
    private val binding: FragmentPhoneNumberBinding by lazy {
        FragmentPhoneNumberBinding.inflate(layoutInflater)
    }
    private val viewModel: PhoneNumberViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }
}