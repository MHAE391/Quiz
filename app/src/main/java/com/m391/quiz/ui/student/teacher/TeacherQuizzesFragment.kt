package com.m391.quiz.ui.student.teacher

import android.graphics.Color
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
import com.m391.quiz.databinding.FragmentTeacherQuizzesBinding
import com.m391.quiz.ui.shared.BaseFragment
import com.m391.quiz.ui.shared.BaseViewModel
import com.m391.quiz.ui.student.home.TeachersAdapter
import com.m391.quiz.utils.Statics.TYPE_STUDENT
import com.m391.quiz.utils.setupLinearRecycler
import kotlinx.coroutines.launch
import java.util.Arrays

class TeacherQuizzesFragment : BaseFragment() {

    private val binding by lazy {
        FragmentTeacherQuizzesBinding.inflate(layoutInflater)
    }
    private val args by navArgs<TeacherQuizzesFragmentArgs>()
    override val viewModel: TeacherQuizzesViewModel by viewModels {
        TeacherQuizzesViewModelFactory(
            requireActivity().application,
            remoteDatabase.quizzes,
            args.teacherId
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setupRecycler()
    }

    private fun setupRecycler() {
        val adapter = QuizAdapter { quiz ->
            findNavController().navigate(
                TeacherQuizzesFragmentDirections.actionTeacherQuizzesFragmentToSolveQuizFragment(
                    quiz
                )
            )
        }
        binding.quizzesRecycler.setupLinearRecycler(adapter, true)
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

}