package com.m391.quiz.ui.authentication.information.student

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import com.m391.quiz.R
import com.m391.quiz.ui.shared.BaseViewModel
import com.m391.quiz.utils.Statics.EMPTY_ACADEMIC_SUBJECTS
import com.m391.quiz.utils.Statics.EMPTY_ACADEMIC_YEAR
import com.m391.quiz.utils.Statics.EMPTY_DATE_OF_BARTH
import com.m391.quiz.utils.Statics.EMPTY_FIRST_NAME_LAST_NAME
import com.m391.quiz.utils.Statics.EMPTY_PROFILE_IMAGE
import com.m391.quiz.utils.Statics.TYPE_STUDENT
import com.m391.quiz.utils.Statics.VALID_DATA
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date

class StudentInformationViewModel(
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

    val studentFirstName = MutableLiveData<String>()
    val studentLastName = MutableLiveData<String>()

    private val _studentDateOfBarth = MutableLiveData<String>()
    val studentDateOfBarth: LiveData<String> = _studentDateOfBarth

    private val _studentAcademicYear = MutableLiveData<String>()
    val studentAcademicYear: LiveData<String> = _studentAcademicYear

    private val _studentProfileImage = MutableLiveData<String>()
    val studentProfileImage: LiveData<String> = _studentProfileImage

    private val _studentSubjects = MutableLiveData<List<String>>()
    val studentSubjects: LiveData<List<String>> = _studentSubjects

    private val _studentYear = MutableLiveData<List<String>>()
    val studentYear: LiveData<List<String>> = _studentYear

    private val _response = MutableLiveData<String>()
    val response: LiveData<String> = _response


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

    suspend fun uploadStudentData() = withContext(Dispatchers.Main) {
        val validData = checkDataValidation()
        if (validData != VALID_DATA) _response.postValue(validData)
        else _response.postValue(
            setUserData(
                TYPE_STUDENT,
                studentProfileImage.value!!,
                studentFirstName.value!!,
                studentLastName.value!!,
                studentSubjects.value!!,
                listOf(studentAcademicYear.value!!),
                studentDateOfBarth.value!!
            )
        )
    }

    fun resetResponse() {
        _response.postValue(String())
    }

    private fun checkDataValidation(): String {
        return if (studentProfileImage.value.isNullOrBlank()) EMPTY_PROFILE_IMAGE
        else if (studentFirstName.value.isNullOrBlank() || studentLastName.value.isNullOrBlank()) EMPTY_FIRST_NAME_LAST_NAME
        else if (studentDateOfBarth.value.equals(app.getString(R.string.select_date_of_barth))) EMPTY_DATE_OF_BARTH
        else if (studentAcademicYear.value.equals(app.getString(R.string.academic_year))) EMPTY_ACADEMIC_YEAR
        else if (studentSubjects.value.isNullOrEmpty()) EMPTY_ACADEMIC_SUBJECTS
        else VALID_DATA
    }
}