package com.m391.quiz.database.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.m391.quiz.models.TeacherFirebaseModel
import com.m391.quiz.utils.Statics.ACADEMIC_SUBJECTS
import com.m391.quiz.utils.Statics.ACADEMIC_YEARS
import com.m391.quiz.utils.Statics.DATE_OF_BARTH
import com.m391.quiz.utils.Statics.FIRST_NAME
import com.m391.quiz.utils.Statics.LAST_NAME
import com.m391.quiz.utils.Statics.PROFILE_IMAGE_PATH
import com.m391.quiz.utils.Statics.PROFILE_IMAGE_URI
import com.m391.quiz.utils.Statics.QUIZ_IMAGE_URL
import com.m391.quiz.utils.Statics.TEACHERS
import com.m391.quiz.utils.Statics.UID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Teachers {
    private val firestore = FirebaseFirestore.getInstance().collection(TEACHERS)
    private var registration: ListenerRegistration? = null

    suspend fun getAllTeachers(): LiveData<List<TeacherFirebaseModel>> =
        withContext(Dispatchers.IO) {
            val teachers = MutableLiveData<List<TeacherFirebaseModel>>()
            registration = firestore.addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Teachers Listener", error.localizedMessage!!)
                    return@addSnapshotListener
                } else {
                    val teachersArray = ArrayList<TeacherFirebaseModel>()
                    value?.forEach { teacher ->
                        teachersArray.add(
                            TeacherFirebaseModel(
                                uid = teacher.get(UID, String::class.java)!!,
                                firstName = teacher.get(FIRST_NAME, String::class.java)!!,
                                lastName = teacher.get(LAST_NAME, String::class.java)!!,
                                dateOfBarth = teacher.get(DATE_OF_BARTH, String::class.java)!!,
                                imageUrl = teacher.get(PROFILE_IMAGE_URI, String::class.java)!!,
                                imagePath = teacher.get(PROFILE_IMAGE_PATH, String::class.java)!!,
                                academicYears = teacher.get(ACADEMIC_YEARS)!! as List<String>,
                                subjects = teacher.get(ACADEMIC_SUBJECTS)!! as List<String>,
                            )
                        )
                        teachers.postValue(teachersArray)
                    }
                }
            }
            return@withContext teachers
        }

    suspend fun getTeacherById(id: String): LiveData<TeacherFirebaseModel> =
        withContext(Dispatchers.IO)
        {
            val teacherInformation = MutableLiveData<TeacherFirebaseModel>()
            registration = firestore.document(id).addSnapshotListener { teacher, error ->
                if (error != null) {
                    Log.e("Teacher Listener", error.localizedMessage!!)
                    return@addSnapshotListener
                } else {
                    if (teacher != null) {
                        val teacherData = TeacherFirebaseModel(
                            uid = teacher.get(UID, String::class.java)!!,
                            firstName = teacher.get(FIRST_NAME, String::class.java)!!,
                            lastName = teacher.get(LAST_NAME, String::class.java)!!,
                            dateOfBarth = teacher.get(DATE_OF_BARTH, String::class.java)!!,
                            imageUrl = teacher.get(PROFILE_IMAGE_URI, String::class.java)!!,
                            imagePath = teacher.get(PROFILE_IMAGE_PATH, String::class.java)!!,
                            academicYears = teacher.get(ACADEMIC_YEARS)!! as List<String>,
                            subjects = teacher.get(ACADEMIC_SUBJECTS)!! as List<String>,
                        )
                        teacherInformation.postValue(teacherData)
                    }
                }
            }
            return@withContext teacherInformation
        }

    suspend fun closeTeachersStream() = withContext(Dispatchers.IO) {
        if (registration != null) {
            registration!!.remove()
        }
    }
}