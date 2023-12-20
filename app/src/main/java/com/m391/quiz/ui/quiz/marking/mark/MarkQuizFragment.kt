package com.m391.quiz.ui.quiz.marking.mark

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.m391.quiz.R
import com.m391.quiz.databinding.FragmentMarkQuizBinding
import com.m391.quiz.ui.quiz.solve.SolveQuizAdapter
import com.m391.quiz.ui.quiz.solve.SolveQuizFragmentDirections
import com.m391.quiz.ui.shared.BaseFragment
import com.m391.quiz.ui.shared.BaseViewModel
import com.m391.quiz.utils.Statics
import com.m391.quiz.utils.getQuestionScore
import com.m391.quiz.utils.setupLinearRecycler
import kotlinx.coroutines.launch

class MarkQuizFragment : BaseFragment() {

    private val binding by lazy {
        FragmentMarkQuizBinding.inflate(layoutInflater)
    }
    private val args by navArgs<MarkQuizFragmentArgs>()
    override val viewModel by viewModels<MarkQuizViewModel> {
        MarkQuizViewModelFactory(
            requireActivity().application,
            args.quiz,
            args.studentUid,
            remoteDatabase.solutions
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
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            viewModel.getStudentScores(viewLifecycleOwner)
            viewModel.refreshQuizQuestions()
        }
    }

    override fun onPause() {
        super.onPause()
        lifecycleScope.launch {
            viewModel.removeStudentScores(viewLifecycleOwner)
        }
    }

    private fun setupRecyclerView() {
        val adapter = SolveQuizAdapter {
            findNavController().navigate(
                MarkQuizFragmentDirections.actionMarkQuizFragmentToMarkQuestionFragment(
                    it,
                    args.studentUid
                )
            )
        }
        binding.quizQuestionsRecycler.setupLinearRecycler(adapter, true)
    }
}