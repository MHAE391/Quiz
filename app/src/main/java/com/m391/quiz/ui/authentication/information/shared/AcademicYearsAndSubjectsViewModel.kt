package com.m391.quiz.ui.authentication.information.shared

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.m391.quiz.R
import com.m391.quiz.models.CheckItem
import com.m391.quiz.ui.shared.BaseViewModel

class AcademicYearsAndSubjectsViewModel(
    private val app: Application,
    private val selectedItems: LiveData<List<String>>,
    private val selectItem: (String) -> Unit,
    private val unSelectItem: (String) -> Unit
) :
    BaseViewModel(app) {
    private val _academicYearsList = MutableLiveData<List<CheckItem>>()
    val academicYearsList: LiveData<List<CheckItem>> = _academicYearsList

    private val _academicSubjectsList = MutableLiveData<List<CheckItem>>()
    val academicSubjectsList: LiveData<List<CheckItem>> = _academicSubjectsList

    fun setItemChecked(checkItem: CheckItem) {
        selectItem(checkItem.item)
    }

    fun setItemUnChecked(checkItem: CheckItem) {
        unSelectItem(checkItem.item)
    }

    private var selectedItem: CheckItem? = null
    fun setYearItemChecked(checkItem: CheckItem) {
        selectItem(checkItem.item)
        selectedItem = checkItem
        _academicYearsList.postValue(
            _academicYearsList.value!!.map { item ->
                CheckItem(
                    item.item,
                    selectedItem != null && selectedItem!!.item == item.item
                )
            })

    }


    fun refreshSubjectData() {
        val subjects = app.resources.getStringArray(R.array.academic_subjects_array).toList()
        _academicSubjectsList.postValue(
            subjects.map { item ->
                CheckItem(
                    item,
                    !selectedItems.value.isNullOrEmpty() && selectedItems.value!!.contains(item)
                )
            }
        )
    }


    fun refreshAcademicYearsData() {
        val years = app.resources.getStringArray(R.array.academic_years_array).toList()
        _academicYearsList.postValue(
            years.map { item ->
                CheckItem(
                    item,
                    !selectedItems.value.isNullOrEmpty() && selectedItems.value!!.contains(item)
                )
            }
        )
    }
}