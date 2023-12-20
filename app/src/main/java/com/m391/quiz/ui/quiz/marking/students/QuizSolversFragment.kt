package com.m391.quiz.ui.quiz.marking.students

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.m391.quiz.databinding.FragmentQuizSolversBinding
import com.m391.quiz.ui.shared.BaseFragment
import com.m391.quiz.utils.setupLinearRecycler
import kotlinx.coroutines.launch

class QuizSolversFragment : BaseFragment() {

    private val binding: FragmentQuizSolversBinding by lazy {
        FragmentQuizSolversBinding.inflate(layoutInflater)
    }

    private val args by navArgs<QuizSolversFragmentArgs>()
    override val viewModel by viewModels<QuizSolversViewModel> {
        QuizSolversViewModelFactory(
            requireActivity().application,
            args.quiz.quiz_id,
            remoteDatabase.solutions,
            remoteDatabase.students
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
            viewModel.refreshResolvers(viewLifecycleOwner)
        }
        viewModel.studentsInformation.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) viewModel.positiveShowNoData()
            else viewModel.negativeShowNoData()
        }
    }

    override fun onPause() {
        super.onPause()
        lifecycleScope.launch {
            viewModel.stopRefresh(viewLifecycleOwner)
            viewModel.studentsInformation.removeObservers(viewLifecycleOwner)
        }
    }

    override fun onStart() {
        super.onStart()
        setupRecycler()
    }

    private fun setupRecycler() {
        val adapter = StudentsAdapter {
            findNavController().navigate(
                QuizSolversFragmentDirections.actionQuizSolversFragmentToMarkQuizFragment(
                    args.quiz,
                    it.uid
                )
            )
        }
        binding.studentsRecycler.setupLinearRecycler(adapter, true)
    }
}