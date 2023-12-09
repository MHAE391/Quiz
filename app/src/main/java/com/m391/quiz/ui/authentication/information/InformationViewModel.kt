package com.m391.quiz.ui.authentication.information

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import com.m391.quiz.database.remote.Information
import com.m391.quiz.ui.shared.BaseViewModel
import com.m391.quiz.utils.m391Capitalize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

class InformationViewModel(private val app: Application, private val information: Information) :
    BaseViewModel(app) {
    private val _switchEnabled = MutableLiveData<Boolean>()
    val switchEnabled: LiveData<Boolean> = _switchEnabled

    init {
        setSwitchAndLoading(true)
    }

    fun setSwitchAndLoading(value: Boolean) {
        _switchEnabled.postValue(value)
        showLoading.postValue(!value)
    }

    val uploadUserData: suspend (
        userType: String,
        profileImage: String,
        firstName: String,
        lastName: String,
        subjects: List<String>,
        academicYears: List<String>,
        dateOfBarth: String
    ) -> String =
        { userType, profileImage, firstName, lastName, subjects, academicYears, dateOfBarth ->
            withContext(Dispatchers.IO) {
                return@withContext information.uploadUserInformation(
                    userType = userType,
                    userFirstName = firstName.m391Capitalize(),
                    userLastName = lastName.m391Capitalize(),
                    userSubjects = subjects,
                    userAcademicYears = academicYears,
                    dateOfBarth = dateOfBarth,
                    profileImageUri = profileImage
                )
            }
        }
}