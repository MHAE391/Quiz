package com.m391.quiz.ui.authentication.information.student

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import com.m391.quiz.R
import com.m391.quiz.ui.shared.BaseViewModel
import java.util.Date

class StudentInformationViewModel(val app: Application) : BaseViewModel(app) {

    val studentFirstName = MutableLiveData<String>()
    val studentLastName = MutableLiveData<String>()
    private val _studentDateOfBarth = MutableLiveData<String>()
    val studentDateOfBarth: LiveData<String> = _studentDateOfBarth
    private val _studentAcademicYear = MutableLiveData<String>()
    val studentAcademicYear: LiveData<String> = _studentAcademicYear

    private val _studentProfileImage = MutableLiveData<String>()
    val studentImageProfile: LiveData<String> = _studentProfileImage
    private val _studentSubjects = MutableLiveData<List<String>>()
    val studentSubjects: LiveData<List<String>> = _studentSubjects

    private val _studentYear = MutableLiveData<List<String>>()
    val studentYear: LiveData<List<String>> = _studentYear

    init {
        _studentAcademicYear.postValue(app.getString(R.string.academic_year))
        _studentDateOfBarth.postValue(app.getString(R.string.select_date_of_barth))
    }

    fun setStudentImage(imageUri: String) {
        _studentProfileImage.postValue(imageUri)
    }

    val setStudentDateOfBarth: (String) -> Unit = { barth ->
        _studentDateOfBarth.postValue(barth)
    }

    val selectSubject: (String) -> Unit = { subject ->
        val oldList =
            if (_studentSubjects.value.isNullOrEmpty()) ArrayList() else _studentSubjects.value!!.toMutableList()
        oldList.add(subject)
        _studentSubjects.postValue(oldList.toList())
    }
    val unSelectSubject: (String) -> Unit = { subject ->
        val oldList =
            if (_studentSubjects.value.isNullOrEmpty()) ArrayList() else _studentSubjects.value!!.toMutableList()
        oldList.remove(subject)
        _studentSubjects.postValue(oldList.toList())
    }
    val selectYear: (String) -> Unit = { year ->
        _studentYear.postValue(listOf(year))
        _studentAcademicYear.postValue(year)
    }

}