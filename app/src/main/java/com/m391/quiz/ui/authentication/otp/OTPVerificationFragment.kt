package com.m391.quiz.ui.authentication.otp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.m391.quiz.databinding.FragmentOtpVerificationBinding
import com.m391.quiz.ui.authentication.AuthenticationViewModel
import com.m391.quiz.ui.authentication.AuthenticationViewModelFactory
import com.m391.quiz.ui.authentication.information.InformationActivity
import com.m391.quiz.ui.shared.BaseFragment
import com.m391.quiz.ui.student.StudentActivity
import com.m391.quiz.ui.teacher.TeacherActivity
import com.m391.quiz.utils.Statics.CODE_SENT
import com.m391.quiz.utils.Statics.RESPONSE_SUCCESS
import com.m391.quiz.utils.Statics.SUCCESSFUL_LOGIN
import kotlinx.coroutines.launch


class OTPVerificationFragment : BaseFragment() {

    private val binding: FragmentOtpVerificationBinding by lazy {
        FragmentOtpVerificationBinding.inflate(layoutInflater)
    }
    private val args: OTPVerificationFragmentArgs by navArgs()
    private val authenticationViewModel: AuthenticationViewModel by activityViewModels {
        AuthenticationViewModelFactory(
            requireActivity().application,
            remoteDatabase.authentication,
            remoteDatabase.information
        )
    }
    override val viewModel: OTPVerificationViewModel by viewModels {
        OTPVerificationViewModelFactory(
            requireActivity().application,
            authenticationViewModel.response,
            args.phoneNumber,
            authenticationViewModel.resendCode,
            authenticationViewModel.verifyCode
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

    override fun onPause() {
        super.onPause()
        authenticationViewModel.authenticationInit()
        viewModel.response.removeObservers(viewLifecycleOwner)
    }

    override fun onResume() {
        super.onResume()
        viewModel.response.observe(viewLifecycleOwner) { response ->
            if (response == SUCCESSFUL_LOGIN) {
                lifecycleScope.launch {
                    if (authenticationViewModel.checkIfTeacher()) {
                        startActivity(Intent(requireActivity(), TeacherActivity::class.java))
                        requireActivity().finish()
                    } else if (authenticationViewModel.checkIfStudent()) {
                        startActivity(Intent(requireActivity(), StudentActivity::class.java))
                        requireActivity().finish()
                    } else {
                        startActivity(Intent(requireActivity(), InformationActivity::class.java))
                        requireActivity().finish()
                    }
                }
            } else if (response == RESPONSE_SUCCESS) {
                viewModel.showToast(CODE_SENT)
                viewModel.resetData()
                restEditTextCodes(true)
                resetData()

            } else if (response != String()) {
                viewModel.resetData()
                resetData()
                restEditTextCodes(true)
                viewModel.showSnackBar(response, requireView())
            }

        }
    }

    override fun onStart() {
        super.onStart()
        setupEditText()
        binding.resendOTPCode.setOnClickListener {
            lifecycleScope.launch {
                viewModel.sendCodeAgain(requireActivity())
                resendOTPCodeVisibility()
            }
        }
        resendOTPCodeVisibility()
        binding.verify.setOnClickListener {
            it.isEnabled = false
            lifecycleScope.launch {
                viewModel.verify(restEditTextCodes)
            }
        }
    }

    private fun setupEditText() {
        viewModel.setTextWatcher(binding.firstCode, binding.secondCode)
        viewModel.setTextWatcher(binding.secondCode, binding.thirdCode)
        viewModel.setTextWatcher(binding.thirdCode, binding.fourthCode)
        viewModel.setTextWatcher(binding.fourthCode, binding.fifthCode)
        viewModel.setTextWatcher(binding.fifthCode, binding.sixthCode)
        viewModel.setLastTextWatcher(binding.sixthCode, binding.verify, restEditTextCodes)
    }

    private val restEditTextCodes: (Boolean) -> Unit =
        { enabled ->
            binding.firstCode.isEnabled = enabled
            binding.secondCode.isEnabled = enabled
            binding.thirdCode.isEnabled = enabled
            binding.fourthCode.isEnabled = enabled
            binding.fifthCode.isEnabled = enabled
            binding.sixthCode.isEnabled = enabled

        }

    private fun resendOTPCodeVisibility() {
        binding.resendOTPCode.visibility = View.GONE
        binding.resendText.visibility = View.GONE
        binding.resendOTPCode.isEnabled = false
        Handler(Looper.myLooper()!!).postDelayed({
            binding.resendOTPCode.visibility = View.VISIBLE
            binding.resendText.visibility = View.VISIBLE
            binding.resendOTPCode.isEnabled = true
        }, 60000)
    }

    private fun resetData() {
        authenticationViewModel.authenticationInit()
        viewModel.negativeShowLoading()
        binding.verify.isEnabled = true
        binding.firstCode.requestFocus()
    }

}