package com.m391.quiz.ui.quiz.create

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.m391.quiz.databinding.FragmentCreateQuizBinding
import com.m391.quiz.ui.shared.BaseFragment
import java.util.Calendar
import androidx.activity.result.PickVisualMediaRequest
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.m391.quiz.R
import com.m391.quiz.database.local.repositories.QuizRepository
import com.m391.quiz.ui.shared.bottom_sheets.AcademicSubjectsFragment
import com.m391.quiz.ui.shared.bottom_sheets.AcademicYearsFragment
import com.m391.quiz.ui.quiz.QuizViewModel
import com.m391.quiz.ui.quiz.QuizViewModelFactory
import com.m391.quiz.utils.Statics.RESPONSE_SUCCESS
import kotlinx.coroutines.launch


class CreateQuizFragment : BaseFragment() {

    private val binding: FragmentCreateQuizBinding by lazy {
        FragmentCreateQuizBinding.inflate(layoutInflater)
    }
    private val teacherViewModel: QuizViewModel by activityViewModels {
        QuizViewModelFactory(requireActivity().application, QuizRepository(requireContext()))
    }
    override val viewModel: CreateQuizViewModel by viewModels {
        CreateQuizViewModelFactory(
            requireActivity().application,
            teacherViewModel.insertQuiz,
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
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.quizDuration.setOnClickListener {
            showTimePickerDialog()
        }
        binding.quizImage.setOnClickListener {
            chooseQuizPlaceholder.launch(
                PickVisualMediaRequest
                    (ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
        binding.quizAcademicSubject.setOnClickListener {
            showSelectSubjectFragment()
        }
        binding.quizAcademicYear.setOnClickListener {
            showSelectAcademicYearFragment()
        }
        binding.arrowBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.createQuiz.setOnClickListener {
            disableUI()
            lifecycleScope.launch {
                viewModel.addQuizToDatabase()
            }
        }
    }

    private val chooseQuizPlaceholder =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewModel.setQuizPlaceholder(uri.toString())
            }
        }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                val selectedTime = "Selected Time = $selectedHour : $selectedMinute"
                val time = convertTimeToMilliseconds(selectedHour, selectedMinute)
                viewModel.setQuizDuration(selectedTime, time)
            },
            hour,
            minute,
            true
        )

        timePickerDialog.show()
    }

    override fun onResume() {
        super.onResume()
        viewModel.response.observe(viewLifecycleOwner) { response ->
            if (!response.isNullOrEmpty()) {
                if (response == RESPONSE_SUCCESS) {
                    viewModel.showToast(response)
                    findNavController().popBackStack()
                } else viewModel.showSnackBar(response, requireView())
                enableUI()
                viewModel.resetResponse()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.response.removeObservers(viewLifecycleOwner)
        viewModel.resetResponse()
    }

    private fun convertTimeToMilliseconds(hours: Int, minutes: Int): Long {
        return (hours * 60 * 60 * 1000 + minutes * 60 * 1000).toLong()
    }

    private fun showSelectAcademicYearFragment() {
        val fragment = AcademicYearsFragment(
            selectItem = viewModel.selectYear,
            unSelectItem = viewModel.selectYear,
            selectedItems = viewModel.selectedYear,
            checkBoxOrRadioButton = false
        )
        fragment.show(parentFragmentManager, getString(R.string.select_quiz_year))
    }

    private fun showSelectSubjectFragment() {
        val fragment = AcademicSubjectsFragment(
            selectItem = viewModel.selectSubject,
            unSelectItem = viewModel.selectSubject,
            selectedItems = viewModel.selectedSubject,
            checkBoxOrRadioButton = false
        )
        fragment.show(parentFragmentManager, getString(R.string.select_quiz_subject))
    }

    private fun disableUI() {
        enableDisableUI(false)
    }

    private fun enableUI() {
        enableDisableUI(true)
    }

    private fun enableDisableUI(value: Boolean) {
        binding.quizImage.isEnabled = value
        binding.quizAcademicSubject.isEnabled = value
        binding.quizAcademicYear.isEnabled = value
        binding.quizDuration.isEnabled = value
        binding.textDescription.isEnabled = value
        binding.createQuiz.isEnabled = value
        viewModel.showLoading.postValue(!value)
    }
}