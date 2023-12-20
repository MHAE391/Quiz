package com.m391.quiz.ui.question.grade

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.m391.quiz.R
import com.m391.quiz.databinding.FragmentQuestionGradeBinding
import com.m391.quiz.ui.shared.BaseFragment
import com.m391.quiz.ui.shared.BaseViewModel
import kotlinx.coroutines.launch

class QuestionGradeFragment : BaseFragment() {
    private val binding by lazy {
        FragmentQuestionGradeBinding.inflate(layoutInflater)
    }
    private val args by navArgs<QuestionGradeFragmentArgs>()
    override val viewModel: QuestionGradeViewModel by viewModels<QuestionGradeViewModel> {
        QuestionGradeViewModelFactory(
            requireActivity().application,
            args.question,
            remoteDatabase.solutions,
            remoteDatabase.authentication.getCurrentUser()!!.uid
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
        viewModel.setQuestionData()
        lifecycleScope.launch {
            viewModel.studentSolution(viewLifecycleOwner)
            viewModel.getQuestionScore(viewLifecycleOwner)
        }
    }

    override fun onPause() {
        super.onPause()
        lifecycleScope.launch {
            viewModel.stopStudentSolution(viewLifecycleOwner)
        }
    }
}