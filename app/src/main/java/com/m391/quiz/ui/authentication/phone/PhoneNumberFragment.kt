package com.m391.quiz.ui.authentication.phone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.m391.quiz.databinding.FragmentPhoneNumberBinding
import com.m391.quiz.ui.authentication.AuthenticationViewModel
import com.m391.quiz.ui.authentication.AuthenticationViewModelFactory
import com.m391.quiz.ui.shared.BaseFragment
import com.m391.quiz.utils.Statics.RESPONSE_SUCCESS
import kotlinx.coroutines.launch

class PhoneNumberFragment : BaseFragment() {
    private val binding: FragmentPhoneNumberBinding by lazy {
        FragmentPhoneNumberBinding.inflate(layoutInflater)
    }
    private val authenticationViewModel: AuthenticationViewModel by activityViewModels {
        AuthenticationViewModelFactory(requireActivity().application, remoteDatabase.authentication)
    }
    override val viewModel: PhoneNumberViewModel by viewModels {
        PhoneNumberViewModelFactory(
            requireActivity().application,
            authenticationViewModel.response,
            authenticationViewModel.getOTPCode
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.getCode.setOnClickListener {
            lifecycleScope.launch {
                viewModel.getCode(
                    requireActivity(), binding.getCode, binding.phoneNumber
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.response.observe(viewLifecycleOwner) {
            if (it == RESPONSE_SUCCESS)
                findNavController().navigate(
                    PhoneNumberFragmentDirections.actionPhoneNumberFragmentToOTPVerificationFragment(
                        viewModel.phoneNumber.value!!
                    )
                )
            else if (it != String()) {
                viewModel.showSnackBar(it, requireView())
                resetDataWithNoObserver()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        resetData()
    }

    private fun resetData() {
        viewModel.response.removeObservers(viewLifecycleOwner)
        resetDataWithNoObserver()
    }

    private fun resetDataWithNoObserver() {
        binding.getCode.isEnabled = true
        binding.phoneNumber.isEnabled = true
        viewModel.negativeShowLoading()
        authenticationViewModel.authenticationInit()
    }
}