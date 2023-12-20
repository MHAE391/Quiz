package com.m391.quiz.ui.student.grades

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.m391.quiz.R
import com.m391.quiz.databinding.FragmentQuizzesAnswersBinding
import com.m391.quiz.ui.shared.BaseFragment
import com.m391.quiz.ui.shared.BaseViewModel
import com.m391.quiz.ui.student.teacher.QuizAdapter
import com.m391.quiz.utils.Statics.STUDENT_SOLVED_QUIZ
import com.m391.quiz.utils.setupLinearRecycler
import kotlinx.coroutines.launch

class QuizzesAnswersFragment : BaseFragment() {
    private val binding by lazy {
        FragmentQuizzesAnswersBinding.inflate(layoutInflater)
    }
    override val viewModel by viewModels<QuizzesAnswersViewModel> {
        QuizzesAnswersViewModelFactory(
            requireActivity().application,
            remoteDatabase.quizzes,
            remoteDatabase.solutions,
            remoteDatabase.authentication.getCurrentUser()!!.uid
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

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            viewModel.refreshQuizzes(viewLifecycleOwner)
        }
    }

    override fun onPause() {
        super.onPause()
        lifecycleScope.launch {
            viewModel.stopRefreshQuizzes(viewLifecycleOwner)
        }
    }

    override fun onStart() {
        super.onStart()
        setupRecycler()
    }

    private fun setupRecycler() {
        val adapter = QuizAdapter {
            findNavController().navigate(
                QuizzesAnswersFragmentDirections.actionQuizzesAnswersFragmentToSolveQuizFragment(
                    it,
                    STUDENT_SOLVED_QUIZ
                )
            )
        }
        binding.quizzesRecycler.setupLinearRecycler(adapter, true)
    }
}