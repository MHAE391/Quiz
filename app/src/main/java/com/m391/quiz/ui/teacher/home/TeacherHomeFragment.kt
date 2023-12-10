package com.m391.quiz.ui.teacher.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.m391.quiz.database.local.repositories.QuizRepository
import com.m391.quiz.databinding.FragmentTeacherHomeBinding
import com.m391.quiz.ui.shared.BaseFragment
import com.m391.quiz.ui.teacher.TeacherViewModel
import com.m391.quiz.ui.teacher.TeacherViewModelFactory
import com.m391.quiz.utils.setupLinearRecycler
import kotlinx.coroutines.launch

class TeacherHomeFragment : BaseFragment() {

    private val binding: FragmentTeacherHomeBinding by lazy {
        FragmentTeacherHomeBinding.inflate(layoutInflater)
    }
    private val teacherViewModel: TeacherViewModel by activityViewModels {
        TeacherViewModelFactory(requireActivity().application, QuizRepository(requireContext()))
    }
    override val viewModel: TeacherHomeViewModel by viewModels {
        TeacherHomeViewModelFactory(requireActivity().application, teacherViewModel.getAllQuizzes)
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
        binding.createNewQuiz.setOnClickListener {
            findNavController().navigate(TeacherHomeFragmentDirections.actionTeacherHomeFragmentToCreateQuizFragment())
        }
    }

    private fun setupRecyclerView() {
        val adapter = QuizzesAdapter {
            binding.unCompletedQuizzesText.text = it.image.toString()
        }
        binding.unCompletedQuizzesRecycler.setupLinearRecycler(adapter, false)
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            viewModel.refreshUnCompletedQuizzes()
        }
    }
}