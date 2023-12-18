package com.m391.quiz.ui.student.home

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.m391.quiz.database.remote.Students
import com.m391.quiz.database.remote.Teachers
import com.m391.quiz.models.StudentFirebaseModel
import com.m391.quiz.models.TeacherFirebaseModel
import com.m391.quiz.ui.shared.BaseViewModel
import kotlinx.coroutines.launch

class StudentHomeViewModel(private val app: Application, private val firebaseTeachers: Teachers) :
    BaseViewModel(app) {
    private val _teachers = MutableLiveData<List<TeacherFirebaseModel>>()
    val teachers: LiveData<List<TeacherFirebaseModel>> = _teachers

    suspend fun refreshTeachers(lifecycleOwner: LifecycleOwner) {
        viewModelScope.launch {
            firebaseTeachers.getAllTeachers().observe(lifecycleOwner) { remoteTeachers ->
                if (!remoteTeachers.isNullOrEmpty()) _teachers.postValue(remoteTeachers)
            }
        }
    }

    suspend fun stopRefreshTeachers(lifecycleOwner: LifecycleOwner) {
        viewModelScope.launch {
            firebaseTeachers.getAllTeachers().removeObservers(lifecycleOwner)
            firebaseTeachers.closeTeachersStream()
        }
    }

}