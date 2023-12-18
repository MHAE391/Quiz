package com.m391.quiz.ui.question.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.m391.quiz.R
import com.m391.quiz.database.local.repositories.QuizRepository
import com.m391.quiz.databinding.FragmentPreviewQuestionBinding
import com.m391.quiz.ui.quiz.QuizViewModel
import com.m391.quiz.ui.quiz.QuizViewModelFactory
import com.m391.quiz.ui.shared.BaseFragment
import kotlinx.coroutines.launch

class PreviewQuestionFragment : BaseFragment() {

    private val binding: FragmentPreviewQuestionBinding by lazy {
        FragmentPreviewQuestionBinding.inflate(layoutInflater)
    }
    private val args: PreviewQuestionFragmentArgs by navArgs()
    private val quizViewModel by activityViewModels<QuizViewModel> {
        QuizViewModelFactory(requireActivity().application, QuizRepository(requireContext()))
    }
    override val viewModel by viewModels<PreviewQuestionViewModel> {
        PreviewQuestionViewModelFactory(
            app = requireActivity().application,
            questionUIModel = args.question,
            quizViewModel.deleteQuestion
        )
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
        if (args.from == getString(R.string.teacher_create)) {
            binding.delete.visibility = View.GONE
        } else binding.delete.visibility = View.VISIBLE

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
        binding.delete.setOnClickListener {
            lifecycleScope.launch {
                viewModel.deleteQuestion()
            }
            findNavController().popBackStack()
        }
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