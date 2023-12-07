package com.m391.quiz.ui.authentication.information.shared

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.m391.quiz.R
import com.m391.quiz.databinding.FragmentAcademicYearsBinding
import com.m391.quiz.ui.authentication.information.student.StudentInformationViewModel
import com.m391.quiz.ui.authentication.information.teacher.TeacherInformationViewModel
import com.m391.quiz.utils.setupLinearRecycler

class AcademicYearsFragment : BottomSheetDialogFragment() {

    private val binding: FragmentAcademicYearsBinding by lazy {
        FragmentAcademicYearsBinding.inflate(layoutInflater)
    }
    private val studentInformationViewModel: StudentInformationViewModel by activityViewModels()
    private val teacherInformationViewModel: TeacherInformationViewModel by activityViewModels()

    val viewModel: AcademicYearsAndSubjectsViewModel by viewModels {
        val teacherOrStudent = this.tag == getString(R.string.select_student_academic)
        AcademicYearsAndSubjectsViewModelFactory(
            requireActivity().application,
            if (teacherOrStudent) studentInformationViewModel.studentYear
            else teacherInformationViewModel.teacherAcademicYear,
            if (teacherOrStudent) studentInformationViewModel.selectYear
            else teacherInformationViewModel.selectYear,
            if (teacherOrStudent) studentInformationViewModel.unSelectSubject
            else teacherInformationViewModel.unSelectYear
        )
    }
    private lateinit var dialog: BottomSheetDialog
    private lateinit var behavior: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        behavior = BottomSheetBehavior.from(view.parent as View)
        behavior.peekHeight = Resources.getSystem().displayMetrics.heightPixels / 2
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        val layout = binding.bottom
        layout.minimumHeight = Resources.getSystem().displayMetrics.heightPixels
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
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val adapter =
            if (this.tag == getString(R.string.select_student_academic)) ItemAdapter(false) { item ->
                viewModel.setYearItemChecked(item)
            }
            else {
                ItemAdapter(true) { item ->
                    if (item.isChecked) viewModel.setItemChecked(item)
                    else viewModel.setItemUnChecked(item)
                }
            }
        binding.subjectsRecycler.setupLinearRecycler(adapter)
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshAcademicYearsData()
    }

}