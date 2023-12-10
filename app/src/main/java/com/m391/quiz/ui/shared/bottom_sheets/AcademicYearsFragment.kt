package com.m391.quiz.ui.shared.bottom_sheets

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.m391.quiz.R
import com.m391.quiz.databinding.FragmentAcademicYearsBinding
import com.m391.quiz.ui.authentication.information.student.StudentInformationViewModel
import com.m391.quiz.ui.authentication.information.teacher.TeacherInformationViewModel
import com.m391.quiz.utils.setupLinearRecycler

class AcademicYearsFragment(
    private val selectedItems: LiveData<List<String>>,
    private val selectItem: (String) -> Unit,
    private val unSelectItem: (String) -> Unit,
    private val checkBoxOrRadioButton: Boolean
) : BottomSheetDialogFragment() {

    private val binding: FragmentAcademicYearsBinding by lazy {
        FragmentAcademicYearsBinding.inflate(layoutInflater)
    }

    val viewModel: AcademicYearsAndSubjectsViewModel by viewModels {
        AcademicYearsAndSubjectsViewModelFactory(
            requireActivity().application,
            selectedItems,
            selectItem,
            unSelectItem
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
        val adapter = ItemAdapter(checkBoxOrRadioButton) { item ->
            if (checkBoxOrRadioButton) {
                if (item.isChecked) viewModel.setItemChecked(item)
                else viewModel.setItemUnChecked(item)
            } else viewModel.setYearItemChecked(item)
        }
        binding.subjectsRecycler.setupLinearRecycler(adapter, true)
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshAcademicYearsData()
    }

}