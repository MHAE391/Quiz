package com.m391.quiz.ui.authentication.information.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.m391.quiz.R
import com.m391.quiz.databinding.FragmentStudentInformationBinding
import com.m391.quiz.ui.authentication.information.InformationViewModel
import com.m391.quiz.ui.authentication.information.setDateOfBarth
import com.m391.quiz.ui.shared.bottom_sheets.AcademicSubjectsFragment
import com.m391.quiz.ui.shared.bottom_sheets.AcademicYearsFragment
import com.m391.quiz.ui.shared.BaseFragment
import com.m391.quiz.utils.Statics.RESPONSE_SUCCESS
import kotlinx.coroutines.launch

class StudentInformationFragment() : BaseFragment() {

    private val binding: FragmentStudentInformationBinding by lazy {
        FragmentStudentInformationBinding.inflate(layoutInflater)
    }
    private val informationViewModel: InformationViewModel by activityViewModels()
    override val viewModel: StudentInformationViewModel by viewModels {
        StudentInformationViewModelFactory(
            requireActivity().application,
            informationViewModel.uploadUserData
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private val chooseStudentPhoto =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewModel.setStudentImage(uri.toString())
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

    override fun onStart() {
        super.onStart()
        binding.studentDateOfBarth.setOnClickListener {
            setDateOfBarth(requireContext(), viewModel.setStudentDateOfBarth)
        }
        binding.studentSubjects.setOnClickListener {
            showSelectSubjectsFragment()
        }

        binding.studentAcademicYear.setOnClickListener {
            showSelectAcademicYearFragment()
        }

        binding.profileImage.setOnClickListener {
            chooseStudentPhoto.launch(
                PickVisualMediaRequest
                    (ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
        binding.submit.setOnClickListener {
            disabledUI()
            lifecycleScope.launch {
                viewModel.uploadStudentData()
            }
        }
    }

    private fun showSelectSubjectsFragment() {
        val fragment = AcademicSubjectsFragment(
            selectItem = viewModel.selectSubject,
            unSelectItem = viewModel.unSelectSubject,
            selectedItems = viewModel.studentSubjects,
            checkBoxOrRadioButton = true
        )
        fragment.show(parentFragmentManager, getString(R.string.select_student_subjects))
    }

    private fun showSelectAcademicYearFragment() {
        val fragment = AcademicYearsFragment(
            selectItem = viewModel.selectYear,
            unSelectItem = viewModel.unSelectSubject,
            selectedItems = viewModel.studentYear,
            checkBoxOrRadioButton = false
        )
        fragment.show(parentFragmentManager, getString(R.string.select_student_academic))
    }

    override fun onResume() {
        super.onResume()
        viewModel.response.observe(viewLifecycleOwner) { response ->
            if (!response.isNullOrEmpty()) {
                if (response == RESPONSE_SUCCESS) {
                    viewModel.showToast(response)
                } else {
                    viewModel.showSnackBar(response, requireView())
                }
                enabledUI()
                viewModel.resetResponse()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.response.removeObservers(viewLifecycleOwner)
        viewModel.resetResponse()
    }

    private fun enabledUI() {
        enableDisableUI(true)
    }

    private fun disabledUI() {
        enableDisableUI(false)
    }

    private fun enableDisableUI(value: Boolean) {
        informationViewModel.setSwitchAndLoading(value)
        binding.studentDateOfBarth.isEnabled = value
        binding.profileImage.isEnabled = value
        binding.submit.isEnabled = value
        binding.studentSubjects.isEnabled = value
        binding.studentAcademicYear.isEnabled = value
        binding.firstName.isEnabled = value
        binding.lastName.isEnabled = value
    }
}