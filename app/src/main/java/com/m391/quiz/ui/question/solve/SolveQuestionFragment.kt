package com.m391.quiz.ui.question.solve

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.m391.quiz.databinding.FragmentSolveQuestionBinding
import com.m391.quiz.ui.shared.BaseFragment
import com.m391.quiz.utils.Statics.BODY_IMAGE_RESTED
import com.m391.quiz.utils.Statics.BODY_IMAGE_SELECTED
import com.m391.quiz.utils.Statics.MCQ_FIRST
import com.m391.quiz.utils.Statics.MCQ_FOURTH
import com.m391.quiz.utils.Statics.MCQ_SECOND
import com.m391.quiz.utils.Statics.MCQ_THIRD
import com.m391.quiz.utils.Statics.RESPONSE_SUCCESS
import kotlinx.coroutines.launch

class SolveQuestionFragment : BaseFragment() {

    private val binding: FragmentSolveQuestionBinding by lazy {
        FragmentSolveQuestionBinding.inflate(layoutInflater)
    }
    private val args by navArgs<SolveQuestionFragmentArgs>()
    override val viewModel by viewModels<SolveQuestionViewModel> {
        SolveQuestionViewModelFactory(
            requireActivity().application,
            args.question,
            remoteDatabase.solutions,
            args.progress,
            args.quizDuration
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
    private val chooseAnswerImage =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewModel.setAnswerImageBody(uri.toString(), BODY_IMAGE_SELECTED)
            }
        }

    override fun onStart() {
        super.onStart()
        binding.firstAnswerText.setRadioButtonChecked(MCQ_FIRST)
        binding.secondAnswerText.setRadioButtonChecked(MCQ_SECOND)
        binding.thirdAnswerText.setRadioButtonChecked(MCQ_THIRD)
        binding.fourthAnswerText.setRadioButtonChecked(MCQ_FOURTH)
        binding.restBodyImage.setOnClickListener {
            viewModel.setAnswerImageBody(null, BODY_IMAGE_RESTED)
        }

        binding.answerImageBody.setOnClickListener {
            chooseAnswerImage.launch(
                PickVisualMediaRequest
                    (ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
        binding.uploadSolution.setOnClickListener {
            lifecycleScope.launch {
                viewModel.uploadQuestionSolution()
            }
            it.isEnabled = false
        }
    }

    private fun RadioButton.setRadioButtonChecked(answer: String) {
        this.setOnClickListener {
            lastCheckedButton?.isChecked = false
            lastCheckedButton = this
            this.isChecked = true
            viewModel.setMCQAnswer(answer)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopProgress()
    }

    override fun onResume() {
        super.onResume()
        viewModel.response.observe(viewLifecycleOwner) { response ->
            if (!response.isNullOrBlank()) {
                viewModel.negativeShowLoading()
                binding.uploadSolution.isEnabled = true
                if (response == RESPONSE_SUCCESS) {
                    viewModel.showToast(RESPONSE_SUCCESS)
                    findNavController().popBackStack()
                } else {
                    viewModel.showSnackBar(response, requireView())
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.response.removeObservers(viewLifecycleOwner)
        viewModel.resetResponse()
    }
}