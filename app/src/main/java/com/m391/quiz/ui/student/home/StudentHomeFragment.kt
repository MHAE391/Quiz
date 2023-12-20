package com.m391.quiz.ui.student.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.m391.quiz.R
import com.m391.quiz.databinding.FragmentStudentHomeBinding
import com.m391.quiz.ui.shared.BaseFragment
import com.m391.quiz.utils.setupGridRecycler
import com.m391.quiz.utils.setupLinearRecycler
import kotlinx.coroutines.launch

class StudentHomeFragment : BaseFragment() {

    private val binding: FragmentStudentHomeBinding by lazy {
        FragmentStudentHomeBinding.inflate(layoutInflater)
    }
    override val viewModel by viewModels<StudentHomeViewModel> {
        StudentHomeViewModelFactory(requireActivity().application, remoteDatabase.teachers)
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

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            viewModel.refreshTeachers(viewLifecycleOwner)
        }
    }

    override fun onStart() {
        super.onStart()
        setupRecycler()
        binding.archive.setOnClickListener {
            findNavController().navigate(StudentHomeFragmentDirections.actionStudentHomeFragmentToQuizzesAnswersFragment())
        }
    }

    private fun setupRecycler() {
        val adapter = TeachersAdapter { teacher ->
            findNavController().navigate(
                StudentHomeFragmentDirections.actionStudentHomeFragmentToTeacherQuizzesFragment(
                    teacher.uid
                )
            )
        }
        binding.teachersRecycler.setupLinearRecycler(adapter, true)
    }

    override fun onPause() {
        super.onPause()
        lifecycleScope.launch {
            viewModel.stopRefreshTeachers(viewLifecycleOwner)
        }
    }
}