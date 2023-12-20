package com.m391.quiz.ui.quiz.marking.students

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.m391.quiz.database.remote.Solutions
import com.m391.quiz.database.remote.Students
import com.m391.quiz.models.StudentFirebaseModel
import com.m391.quiz.ui.shared.BaseViewModel

class QuizSolversViewModel(
    private val app: Application,
    private val quizId: String,
    private val solutions: Solutions,
    private val students: Students
) : BaseViewModel(app) {
    private val _studentsInformation = MutableLiveData<List<StudentFirebaseModel>>()
    val studentsInformation: LiveData<List<StudentFirebaseModel>> = _studentsInformation

    private val _solvers = MutableLiveData<List<String>>()

    suspend fun refreshResolvers(lifecycleOwner: LifecycleOwner) {
        positiveShowNoData()
        solutions.getAllQuizStudentsSolvers(quizId).observe(lifecycleOwner) {
            _solvers.postValue(it)
            _studentsInformation.postValue(_studentsInformation.value?.filter { id -> it.contains(id.uid) })
        }
        students.getAllStudents().observe(lifecycleOwner) {
            if (!_solvers.value.isNullOrEmpty()) {
                _studentsInformation.postValue(it.filter { id ->
                    _solvers.value!!.contains(id.uid)
                })
            }
        }

    }

    suspend fun stopRefresh(lifecycleOwner: LifecycleOwner) {
        solutions.getAllQuizStudentsSolvers(quizId).removeObservers(lifecycleOwner)
        students.getAllStudents().removeObservers(lifecycleOwner)
        students.closeStudentsStream()
        solutions.closeSolutionsStream()
    }
}