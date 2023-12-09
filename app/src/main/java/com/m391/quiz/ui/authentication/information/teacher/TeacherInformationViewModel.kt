package com.m391.quiz.ui.authentication.information.teacher

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.m391.quiz.R
import com.m391.quiz.ui.shared.BaseViewModel
import com.m391.quiz.utils.Statics.EMPTY_ACADEMIC_SUBJECTS
import com.m391.quiz.utils.Statics.EMPTY_ACADEMIC_YEARS
import com.m391.quiz.utils.Statics.EMPTY_DATE_OF_BARTH
import com.m391.quiz.utils.Statics.EMPTY_FIRST_NAME_LAST_NAME
import com.m391.quiz.utils.Statics.EMPTY_PROFILE_IMAGE
import com.m391.quiz.utils.Statics.TYPE_TEACHER
import com.m391.quiz.utils.Statics.VALID_DATA
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TeacherInformationViewModel(
    private val app: Application,
    private val setUserData: suspend (
        userType: String,
        profileImage: String,
        firstName: String,
        lastName: String,
        subjects: List<String>,
        academicYears: List<String>,
        dateOfBarth: String
    ) -> String
) : BaseViewModel(app) {

    val teacherFirstName = MutableLiveData<String>()
    val teacherLastName = MutableLiveData<String>()
    private val _teacherDateOfBarth = MutableLiveData<String>()
    val teacherDateOfBarth: LiveData<String> = _teacherDateOfBarth
    private val _teacherAcademicYears = MutableLiveData<List<String>>()
    val teacherAcademicYears: LiveData<List<String>> = _teacherAcademicYears

    private val _teacherProfileImage = MutableLiveData<String>()
    val teacherProfileImage: LiveData<String> = _teacherProfileImage

    private val _teacherSubjects = MutableLiveData<List<String>>()
    val teacherSubjects: LiveData<List<String>> = _teacherSubjects

    private val _response = MutableLiveData<String>()
    val response: LiveData<String> = _response

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

    suspend fun uploadStudentData() = withContext(Dispatchers.Main) {
        val validData = checkDataValidation()
        if (validData != VALID_DATA) _response.postValue(validData)
        else _response.postValue(
            setUserData(
                TYPE_TEACHER,
                teacherProfileImage.value!!,
                teacherFirstName.value!!,
                teacherLastName.value!!,
                teacherSubjects.value!!,
                teacherAcademicYears.value!!,
                teacherDateOfBarth.value!!
            )
        )
    }

    fun resetResponse() {
        _response.postValue(String())
    }

    private fun checkDataValidation(): String {
        return if (_teacherProfileImage.value.isNullOrBlank()) EMPTY_PROFILE_IMAGE
        else if (teacherFirstName.value.isNullOrBlank() || teacherLastName.value.isNullOrBlank()) EMPTY_FIRST_NAME_LAST_NAME
        else if (_teacherDateOfBarth.value.equals(app.getString(R.string.select_date_of_barth))) EMPTY_DATE_OF_BARTH
        else if (_teacherAcademicYears.value.isNullOrEmpty()) EMPTY_ACADEMIC_YEARS
        else if (_teacherSubjects.value.isNullOrEmpty()) EMPTY_ACADEMIC_SUBJECTS
        else VALID_DATA
    }
}