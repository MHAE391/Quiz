package com.m391.quiz.ui.authentication.information.teacher

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.m391.quiz.R
import com.m391.quiz.ui.shared.BaseViewModel

class TeacherInformationViewModel(private val app: Application) : BaseViewModel(app) {

    val teacherFirstName = MutableLiveData<String>()
    val teacherLastName = MutableLiveData<String>()
    private val _teacherDateOfBarth = MutableLiveData<String>()
    val teacherDateOfBarth: LiveData<String> = _teacherDateOfBarth
    private val _teacherAcademicYears = MutableLiveData<List<String>>()
    val teacherAcademicYear: LiveData<List<String>> = _teacherAcademicYears

    private val _teacherProfileImage = MutableLiveData<String>()
    val teacherImageProfile: LiveData<String> = _teacherProfileImage

    private val _teacherSubjects = MutableLiveData<List<String>>()
    val teacherSubjects: LiveData<List<String>> = _teacherSubjects

    init {
        _teacherDateOfBarth.postValue(app.getString(R.string.select_date_of_barth))
    }

    fun setTeacherImage(imageUri: String) {
        _teacherProfileImage.postValue(imageUri)
    }

    val setTeacherDateOfBarth: (String) -> Unit = { barth ->
        _teacherDateOfBarth.postValue(barth)
    }

    val selectSubject: (String) -> Unit = { subject ->
        val oldList =
            if (_teacherSubjects.value.isNullOrEmpty()) ArrayList() else _teacherSubjects.value!!.toMutableList()
        oldList.add(subject)
        _teacherSubjects.postValue(oldList.toList())
    }
    val unSelectSubject: (String) -> Unit = { subject ->
        val oldList =
            if (_teacherSubjects.value.isNullOrEmpty()) ArrayList() else _teacherSubjects.value!!.toMutableList()
        oldList.remove(subject)
        _teacherSubjects.postValue(oldList.toList())
    }

    val selectYear: (String) -> Unit = { year ->
        val oldList =
            if (_teacherAcademicYears.value.isNullOrEmpty()) ArrayList() else _teacherAcademicYears.value!!.toMutableList()
        oldList.add(year)
        _teacherAcademicYears.postValue(oldList.toList())
    }
    val unSelectYear: (String) -> Unit = { year ->
        val oldList =
            if (_teacherAcademicYears.value.isNullOrEmpty()) ArrayList() else _teacherAcademicYears.value!!.toMutableList()
        oldList.remove(year)
        _teacherAcademicYears.postValue(oldList.toList())
    }
}