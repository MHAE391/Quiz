package com.m391.quiz.ui.authentication.information.student

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.m391.quiz.R
import com.m391.quiz.ui.shared.BaseViewModel
import java.util.Date

class StudentInformationViewModel(val app: Application) : BaseViewModel(app) {

    val studentFirstName = MutableLiveData<String>()
    val studentLastName = MutableLiveData<String>()
    private val _studentDateOfBarth = MutableLiveData<Date>()
    private val _studentAcademicYear = MutableLiveData<String>()
    val studentAcademicYear: LiveData<String> = _studentAcademicYear
    private val _studentProfileImage = MutableLiveData<String>()

    init {
        _studentAcademicYear.postValue(app.getString(R.string.select_student_academic_year))
    }

    fun setStudentImage(imageUri: String) {
        _studentProfileImage.postValue(imageUri)
    }
}