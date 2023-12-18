package com.m391.quiz.ui.quiz.solve

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.m391.quiz.R
import com.m391.quiz.databinding.FragmentSolveQuizBinding
import com.m391.quiz.ui.quiz.preview.QuestionAdapter
import com.m391.quiz.ui.shared.BaseFragment
import com.m391.quiz.ui.shared.BaseViewModel
import com.m391.quiz.utils.setupLinearRecycler
import kotlinx.coroutines.launch

class SolveQuizFragment : BaseFragment() {

    private val binding: FragmentSolveQuizBinding by lazy {
        FragmentSolveQuizBinding.inflate(layoutInflater)
    }
    private val args by navArgs<SolveQuizFragmentArgs>()
    override val viewModel by viewModels<SolveQuizViewModel> {
        SolveQuizViewModelFactory(
            requireActivity().application,
            args.quiz
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
        binding.uploadSolution.visibility = View.GONE
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshQuestions()
    }

    override fun onStart() {
        super.onStart()
        setupRecyclerView()
        binding.startQuiz.setOnClickListener {
            viewModel.startProgress()
            it.isEnabled = false
            binding.uploadSolution.visibility = View.VISIBLE
        }
    }

    private fun setupRecyclerView() {
        val adapter = SolveQuizAdapter {
            findNavController().navigate(
                SolveQuizFragmentDirections.actionSolveQuizFragmentToSolveQuestionFragment(
                    it
                )
            )
        }
        binding.questionsRecycler.setupLinearRecycler(adapter, true)
    }
}