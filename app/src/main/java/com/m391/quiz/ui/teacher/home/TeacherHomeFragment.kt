package com.m391.quiz.ui.teacher.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.m391.quiz.R
import com.m391.quiz.database.local.entities.Quiz
import com.m391.quiz.database.local.repositories.QuizRepository
import com.m391.quiz.databinding.FragmentTeacherHomeBinding
import com.m391.quiz.models.QuizFirebaseModel
import com.m391.quiz.ui.shared.BaseFragment
import com.m391.quiz.ui.quiz.QuizViewModel
import com.m391.quiz.ui.quiz.QuizViewModelFactory
import com.m391.quiz.utils.setupLinearRecycler
import kotlinx.coroutines.launch

class TeacherHomeFragment : BaseFragment() {

    private val binding: FragmentTeacherHomeBinding by lazy {
        FragmentTeacherHomeBinding.inflate(layoutInflater)
    }
    private val teacherViewModel: QuizViewModel by activityViewModels {
        QuizViewModelFactory(requireActivity().application, QuizRepository(requireContext()))
    }
    override val viewModel: TeacherHomeViewModel by viewModels {
        TeacherHomeViewModelFactory(
            requireActivity().application,
            teacherViewModel.getAllQuizzes,
            remoteDatabase.quizzes,
            remoteDatabase.authentication.getCurrentUser()!!.uid
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setupRecyclerView()
        viewModel.positiveShowNoData()
        binding.unCompletedQuizzesLayout.visibility = View.GONE
        binding.completedQuizzesLayout.visibility = View.GONE

        binding.createNewQuiz.setOnClickListener {
            findNavController().navigate(TeacherHomeFragmentDirections.actionTeacherHomeFragmentToCreateQuizFragment())
        }
    }

    private fun setupRecyclerView() {
        val unCompletedQuizzesAdapter = QuizzesAdapter<Quiz>(R.layout.quiz_item) { quiz ->
            findNavController().navigate(
                TeacherHomeFragmentDirections
                    .actionTeacherHomeFragmentToPreviewQuizFragment(
                        quiz.id
                    )
            )
        }
        binding.unCompletedQuizzesRecycler.setupLinearRecycler(unCompletedQuizzesAdapter, false)
        val completedQuizzesAdapter =
            QuizzesAdapter<QuizFirebaseModel>(R.layout.firebase_quiz_item) { quiz ->
                findNavController().navigate(
                    TeacherHomeFragmentDirections.actionTeacherHomeFragmentToQuizSolversFragment(
                        quiz
                    )
                )
            }
        binding.completedQuizzesRecycler.setupLinearRecycler(completedQuizzesAdapter, true)
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            viewModel.refreshUnCompletedQuizzes()
            viewModel.refreshQuizzes(viewLifecycleOwner)
        }
        viewModel.unCompletedQuizzes.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                viewModel.negativeShowNoData()
                binding.unCompletedQuizzesLayout.visibility = View.VISIBLE
            }
        }
        viewModel.completedQuizzes.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                viewModel.negativeShowNoData()
                binding.completedQuizzesLayout.visibility = View.VISIBLE
            }
        }
    }

    override fun onPause() {
        super.onPause()
        lifecycleScope.launch {
            viewModel.stopRefreshQuizzes(viewLifecycleOwner)
        }
    }
}