package com.m391.quiz.ui.shared.bottom_sheets

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AcademicYearsAndSubjectsViewModelFactory(
    private val app: Application,
    private val selectedItems:LiveData< List<String>>,
    private val selectItem: (String) -> Unit,
    private val unSelectItem: (String) -> Unit
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AcademicYearsAndSubjectsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AcademicYearsAndSubjectsViewModel(
                app,
                selectedItems,
                selectItem,
                unSelectItem
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
