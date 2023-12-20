package com.m391.quiz.ui.question.marking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.m391.quiz.R
import com.m391.quiz.databinding.FragmentMarkQuestionBinding
import com.m391.quiz.databinding.FragmentMarkQuizBinding
import com.m391.quiz.ui.shared.BaseFragment
import com.m391.quiz.ui.shared.BaseViewModel
import com.m391.quiz.utils.Statics.RESPONSE_SUCCESS
import kotlinx.coroutines.launch


class MarkQuestionFragment : BaseFragment() {

    private val binding by lazy {
        FragmentMarkQuestionBinding.inflate(layoutInflater)
    }

    private val args by navArgs<MarkQuestionFragmentArgs>()
    override val viewModel by viewModels<MarkQuestionViewModel> {
        MarkQuestionViewModelFactory(
            requireActivity().application,
            args.question,
            remoteDatabase.solutions,
            args.studentUid
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (args.question.answerType == "MCQ") {
            binding.essayAnswer.visibility = View.GONE
            binding.mcqGroup.visibility = View.VISIBLE
        } else {
            binding.essayAnswer.visibility = View.VISIBLE
            binding.mcqGroup.visibility = View.GONE
            if (!args.question.answerBodyImage)
                binding.answerImageBodyLayout.visibility = View.GONE
            if (!args.question.answerBodyText)
                binding.answerBody.visibility = View.GONE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            viewModel.studentSolution(viewLifecycleOwner)
            viewModel.getQuestionScore(viewLifecycleOwner)
        }
        viewModel.response.observe(viewLifecycleOwner) { response ->
            if (!response.isNullOrBlank()) {
                if (response == RESPONSE_SUCCESS) {
                    viewModel.showToast(RESPONSE_SUCCESS)
                    findNavController().popBackStack()
                } else viewModel.showSnackBar(response, requireView())
                viewModel.negativeShowLoading()
                viewModel.resetResponse()
                binding.uploadMarking.isEnabled = true
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.uploadMarking.setOnClickListener {
            it.isEnabled = false
            lifecycleScope.launch {
                viewModel.uploadScore()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        lifecycleScope.launch {
            viewModel.stopStudentSolution(viewLifecycleOwner)
            viewModel.response.removeObservers(viewLifecycleOwner)
            viewModel.resetResponse()
        }
    }

}