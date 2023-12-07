package com.m391.quiz.ui.authentication.information.teacher

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.m391.quiz.R
import com.m391.quiz.databinding.FragmentTeacherInformationBinding
import com.m391.quiz.ui.authentication.information.setDateOfBarth
import androidx.activity.result.PickVisualMediaRequest
import com.m391.quiz.ui.authentication.information.shared.AcademicSubjectsFragment
import com.m391.quiz.ui.authentication.information.shared.AcademicYearsFragment

import com.m391.quiz.ui.shared.BaseFragment
import com.m391.quiz.ui.shared.BaseViewModel
import com.m391.quiz.utils.Binding

class TeacherInformationFragment : BaseFragment() {

    private val binding: FragmentTeacherInformationBinding by lazy {
        FragmentTeacherInformationBinding.inflate(layoutInflater)
    }

    override val viewModel: TeacherInformationViewModel by activityViewModels()
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

    private val chooseTeacherPhoto =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewModel.setTeacherImage(uri.toString())
            }
        }

    override fun onStart() {
        super.onStart()
        binding.teacherDateOfBarth.setOnClickListener {
            setDateOfBarth(
                requireContext(),
                viewModel.setTeacherDateOfBarth
            )
        }
        binding.teacherSubjects.setOnClickListener {
            showSelectSubjectsFragment()
        }

        binding.teacherAcademicYears.setOnClickListener {
            showSelectAcademicYearFragment()
        }

        binding.profileImage.setOnClickListener {
            chooseTeacherPhoto.launch(
                PickVisualMediaRequest
                    (ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
        binding.submit.setOnClickListener {

        }
    }

    private fun showSelectAcademicYearFragment() {
        val fragment = AcademicYearsFragment()
        fragment.show(parentFragmentManager, getString(R.string.select_teacher_academic))
    }

    private fun showSelectSubjectsFragment() {
        val fragment = AcademicSubjectsFragment()
        fragment.show(parentFragmentManager, getString(R.string.select_teacher_subjects))
    }

    override fun onResume() {
        super.onResume()
        viewModel.teacherImageProfile.observe(viewLifecycleOwner) {
            Binding.loadImage(binding.profileImage, it.toString())
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.teacherImageProfile.removeObservers(viewLifecycleOwner)
    }
}