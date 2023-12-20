package com.m391.quiz.database.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.m391.quiz.models.StudentFirebaseModel
import com.m391.quiz.models.TeacherFirebaseModel
import com.m391.quiz.utils.Statics
import com.m391.quiz.utils.Statics.ACADEMIC_SUBJECTS
import com.m391.quiz.utils.Statics.ACADEMIC_YEARS
import com.m391.quiz.utils.Statics.PROFILE_IMAGE_PATH
import com.m391.quiz.utils.Statics.PROFILE_IMAGE_URI
import com.m391.quiz.utils.Statics.STUDENTS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Students {
    private val firestore = FirebaseFirestore.getInstance().collection(STUDENTS)
    private var registration: ListenerRegistration? = null

    suspend fun getAllStudents(): LiveData<List<StudentFirebaseModel>> =
        withContext(Dispatchers.IO) {
            val students = MutableLiveData<List<StudentFirebaseModel>>()
            registration = firestore.addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Students Listener", error.localizedMessage!!)
                    return@addSnapshotListener
                } else {

                    if (value != null) {
                        val studentsArray = ArrayList<StudentFirebaseModel>()
                        value.forEach { student ->
                            studentsArray.add(
                                StudentFirebaseModel(
                                    uid = student.get(Statics.UID, String::class.java)!!,
                                    firstName = student.get(
                                        Statics.FIRST_NAME,
                                        String::class.java
                                    )!!,
                                    lastName = student.get(Statics.LAST_NAME, String::class.java)!!,
                                    dateOfBarth = student.get(
                                        Statics.DATE_OF_BARTH,
                                        String::class.java
                                    )!!,
                                    imageUrl = student.get(
                                        PROFILE_IMAGE_URI,
                                        String::class.java
                                    )!!,
                                    imagePath = student.get(
                                        PROFILE_IMAGE_PATH,
                                        String::class.java
                                    )!!,
                                    academicYear = student.get(
                                        Statics.ACADEMIC_YEAR,
                                        String::class.java
                                    )!!,
                                    subjects = student.get(ACADEMIC_SUBJECTS)!! as List<String>
                                )
                            )
                            students.postValue(studentsArray)
                        }
                    }
                }
            }
            return@withContext students
        }

    suspend fun getStudentById(id: String): LiveData<StudentFirebaseModel> =
        withContext(Dispatchers.IO)
        {
            val studentInformation = MutableLiveData<StudentFirebaseModel>()
            registration = firestore.document(id).addSnapshotListener { student, error ->
                if (error != null) {
                    Log.e("Student Listener", error.localizedMessage!!)
                    return@addSnapshotListener
                } else {
                    if (student != null) {
                        val studentData = StudentFirebaseModel(
                            uid = student.get(Statics.UID, String::class.java)!!,
                            firstName = student.get(Statics.FIRST_NAME, String::class.java)!!,
                            lastName = student.get(Statics.LAST_NAME, String::class.java)!!,
                            dateOfBarth = student.get(
                                Statics.DATE_OF_BARTH,
                                String::class.java
                            )!!,
                            imageUrl = student.get(
                                PROFILE_IMAGE_URI,
                                String::class.java
                            )!!,
                            imagePath = student.get(
                                PROFILE_IMAGE_PATH,
                                String::class.java
                            )!!,
                            academicYear = student.get(
                                ACADEMIC_YEARS,
                                String::class.java
                            )!!,
                            subjects = student.get(ACADEMIC_SUBJECTS)!! as List<String>
                        )
                        studentInformation.postValue(studentData)
                    }
                }
            }
            return@withContext studentInformation
        }

    suspend fun closeStudentsStream() = withContext(Dispatchers.IO) {
        if (registration != null) {
            registration!!.remove()
        }
    }
}