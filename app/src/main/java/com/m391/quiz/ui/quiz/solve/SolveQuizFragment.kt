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
import com.m391.quiz.utils.Statics.SHOW_QUESTION_ERROR
import com.m391.quiz.utils.Statics.SOLVER_ERROR_RESPONSE
import com.m391.quiz.utils.Statics.SOLVER_SUCCESS_RESPONSE
import com.m391.quiz.utils.Statics.STUDENT_SOLVED_QUIZ
import com.m391.quiz.utils.Statics.STUDENT_UNSOLVED_QUIZ
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
            args.quiz,
            remoteDatabase.solutions,
            remoteDatabase.authentication.getCurrentUser()!!.uid
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (args.destination == STUDENT_SOLVED_QUIZ) {
            binding.startQuiz.visibility = View.GONE
            binding.progress.visibility = View.GONE
            binding.score.visibility = View.VISIBLE
        } else {
            binding.score.visibility = View.GONE
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
        viewModel.refreshQuestions()
        if (args.destination == STUDENT_UNSOLVED_QUIZ) {
            viewModel.response.observe(viewLifecycleOwner) { response ->
                if (!response.isNullOrBlank()) {
                    viewModel.resetResponse()
                    viewModel.negativeShowLoading()
                    when (response) {
                        SOLVER_SUCCESS_RESPONSE -> viewModel.startProgress()
                        SOLVER_ERROR_RESPONSE -> {
                            viewModel.showSnackBar(
                                SOLVER_ERROR_RESPONSE,
                                requireView()
                            )
                            binding.startQuiz.isEnabled = true
                        }
                    }
                }
            }
        } else {
            lifecycleScope.launch {
                viewModel.getStudentScores(viewLifecycleOwner)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        setupRecyclerView()
        binding.startQuiz.setOnClickListener {
            viewModel.startQuiz()
            it.isEnabled = false
        }

    }

    override fun onPause() {
        super.onPause()


        if (args.destination == STUDENT_SOLVED_QUIZ) {
            lifecycleScope.launch {
                viewModel.removeStudentScores(viewLifecycleOwner)
            }
        } else {
            viewModel.response.removeObservers(viewLifecycleOwner)
            viewModel.resetResponse()
        }
    }

    private fun setupRecyclerView() {
        val adapter = SolveQuizAdapter {
            if (args.destination == STUDENT_SOLVED_QUIZ) {
                findNavController().navigate(
                    SolveQuizFragmentDirections.actionSolveQuizFragmentToQuestionGradeFragment(
                        it
                    )
                )
            } else {
                if (viewModel.quizStarted.value == true)
                    findNavController().navigate(
                        SolveQuizFragmentDirections.actionSolveQuizFragmentToSolveQuestionFragment(
                            it,
                            viewModel.quizProgress.value!!,
                            quizDuration = args.quiz.quiz_duration
                        )
                    )
                else viewModel.showSnackBar(SHOW_QUESTION_ERROR, requireView())
            }
        }
        binding.questionsRecycler.setupLinearRecycler(adapter, true)
    }
}