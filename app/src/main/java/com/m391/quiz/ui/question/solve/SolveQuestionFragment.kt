package com.m391.quiz.ui.question.solve

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.m391.quiz.databinding.FragmentSolveQuestionBinding
import com.m391.quiz.ui.shared.BaseFragment

class SolveQuestionFragment : BaseFragment() {

    private val binding: FragmentSolveQuestionBinding by lazy {
        FragmentSolveQuestionBinding.inflate(layoutInflater)
    }
    private val args by navArgs<SolveQuestionFragmentArgs>()
    override val viewModel by viewModels<SolveQuestionViewModel> {
        SolveQuestionViewModelFactory(requireActivity().application, args.question)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
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
        return binding.root
    }

    private var lastCheckedButton: RadioButton? = null

    override fun onStart() {
        super.onStart()
        binding.firstAnswerText.setRadioButtonChecked()
        binding.secondAnswerText.setRadioButtonChecked()
        binding.thirdAnswerText.setRadioButtonChecked()
        binding.fourthAnswerText.setRadioButtonChecked()
    }

    private fun RadioButton.setRadioButtonChecked() {
        this.setOnClickListener {
            lastCheckedButton?.isChecked = false
            lastCheckedButton = this
            this.isChecked = true
        }
    }
}