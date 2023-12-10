package com.m391.quiz.database.remote

import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.m391.quiz.utils.Statics.ACADEMIC_SUBJECTS
import com.m391.quiz.utils.Statics.ACADEMIC_YEAR
import com.m391.quiz.utils.Statics.ACADEMIC_YEARS
import com.m391.quiz.utils.Statics.DATE_OF_BARTH
import com.m391.quiz.utils.Statics.FIRST_NAME
import com.m391.quiz.utils.Statics.LAST_NAME
import com.m391.quiz.utils.Statics.PROFILE_IMAGE_PATH
import com.m391.quiz.utils.Statics.PROFILE_IMAGE_URI
import com.m391.quiz.utils.Statics.RESPONSE_SUCCESS
import com.m391.quiz.utils.Statics.STUDENTS
import com.m391.quiz.utils.Statics.TEACHERS
import com.m391.quiz.utils.Statics.TYPE_STUDENT
import com.m391.quiz.utils.Statics.UID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class Information(private val auth: Authentication, private val media: MediaStorage) {

    private val firestore = FirebaseFirestore.getInstance()
    suspend fun uploadUserInformation(
        userType: String,
        profileImageUri: String,
        userFirstName: String,
        userLastName: String,
        userSubjects: List<String>,
        userAcademicYears: List<String>,
        dateOfBarth: String
    ): String = withContext(Dispatchers.IO) {
        var response: String = RESPONSE_SUCCESS
        val currentUserUid = auth.getCurrentUser()!!.uid
        val image = media.uploadImage(profileImageUri, userType)
        val user = hashMapOf(
            UID to currentUserUid,
            FIRST_NAME to userFirstName,
            LAST_NAME to userLastName,
            PROFILE_IMAGE_PATH to image.first,
            PROFILE_IMAGE_URI to image.second,
            ACADEMIC_SUBJECTS to userSubjects,
            if (userType == TYPE_STUDENT) ACADEMIC_YEAR to userAcademicYears.first() else
                ACADEMIC_YEARS to userAcademicYears,
            DATE_OF_BARTH to dateOfBarth
        )
        firestore
            .collection(
                if (userType == TYPE_STUDENT) STUDENTS
                else TEACHERS
            )
            .document(currentUserUid)
            .set(user).addOnFailureListener {
                response = it.message!!
            }.await()
        if (response == RESPONSE_SUCCESS) updateCurrentUserData(
            firstName = userFirstName,
            lastName = userLastName
        )
        return@withContext response
    }

    private suspend fun updateCurrentUserData(firstName: String, lastName: String): Unit =
        withContext(Dispatchers.IO) {
            val user = auth.getCurrentUser()
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName("$firstName $lastName")
                .build()
            user?.updateProfile(profileUpdates)!!.await()
        }

    suspend fun checkAlreadyTeacherOrNot(): Boolean = withContext(Dispatchers.IO) {
        val currentUser = auth.getCurrentUser()
        if (currentUser != null) {
            val teacherUid = currentUser.uid
            val response = firestore.collection(TEACHERS).document(teacherUid).get().await()
            return@withContext response.exists()
        }
        return@withContext false
    }

    suspend fun checkAlreadyStudentOrNot(): Boolean = withContext(Dispatchers.IO) {
        val currentUser = auth.getCurrentUser()
        if (currentUser != null) {
            val teacherUid = currentUser.uid
            val response = firestore.collection(STUDENTS).document(teacherUid).get().await()
            return@withContext response.exists()
        }
        return@withContext false
    }
}