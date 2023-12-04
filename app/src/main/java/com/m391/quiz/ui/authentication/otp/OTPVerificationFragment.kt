package com.m391.quiz.ui.authentication.otp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.m391.quiz.R
import com.m391.quiz.databinding.FragmentOtpVerificationBinding


class OTPVerificationFragment : Fragment() {

    private val binding: FragmentOtpVerificationBinding by lazy {
        FragmentOtpVerificationBinding.inflate(layoutInflater)
    }
    private val viewModel: OTPVerificationViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }
}