package com.m391.quiz.ui.quiz.create

import android.app.Application
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.m391.quiz.R
import com.m391.quiz.database.local.entities.Quiz
import com.m391.quiz.ui.shared.BaseViewModel
import com.m391.quiz.utils.Statics.EMPTY_QUIZ_ACADEMIC_SUBJECTS
import com.m391.quiz.utils.Statics.EMPTY_QUIZ_ACADEMIC_YEAR
import com.m391.quiz.utils.Statics.EMPTY_QUIZ_DESCRIPTION
import com.m391.quiz.utils.Statics.EMPTY_QUIZ_DURATION
import com.m391.quiz.utils.Statics.EMPTY_QUIZ_PLACEHOLDER_IMAGE
import com.m391.quiz.utils.Statics.RESPONSE_SUCCESS
import com.m391.quiz.utils.Statics.VALID_DATA
import com.m391.quiz.utils.m391Capitalize
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.IOException

class CreateQuizViewModel(
    private val app: Application,
    private val createQuiz: suspend (quiz: Quiz) -> Unit,
    private val currentUserUID: String
) : BaseViewModel(app) {


    val quizDescription = MutableLiveData<String>()

    private val _quizPlaceholder = MutableLiveData<String>()
    val quizPlaceholder: LiveData<String> = _quizPlaceholder

    private val _quizAcademicYear = MutableLiveData<String>()
    val quizAcademicYear: LiveData<String> = _quizAcademicYear

    private val _quizAcademicSubject = MutableLiveData<String>()
    val quizAcademicSubject: LiveData<String> = _quizAcademicSubject

    private val _quizDurationString = MutableLiveData<String>()
    val quizDurationString: LiveData<String> = _quizDurationString

    private val _quizDurationLong = MutableLiveData<Long>()

    private val _response = MutableLiveData<String>()
    val response: LiveData<String> = _response

    init {
        _quizAcademicYear.postValue(app.getString(R.string.academic_year))
        _quizAcademicSubject.postValue(app.getString(R.string.academic_subject))
        _quizDurationString.postValue(app.getString(R.string.select_quiz_duration))
    }

    fun setQuizDuration(selectedTime: String, time: Long) {
        _quizDurationString.postValue(selectedTime)
        _quizDurationLong.postValue(time)
    }

    fun setQuizPlaceholder(image: String) {
        _quizPlaceholder.postValue(image)
    }

    fun resetResponse() {
        _response.postValue(String())
    }

    val selectYear: (String) -> Unit = { year ->
        selectedYear.postValue(listOf(year))
        _quizAcademicYear.postValue(year)
    }
    val selectSubject: (String) -> Unit = { subject ->
        selectedSubject.postValue(listOf(subject))
        _quizAcademicSubject.postValue(subject)
    }
    val selectedYear = MutableLiveData<List<String>>()
    val selectedSubject = MutableLiveData<List<String>>()

    suspend fun addQuizToDatabase() {
        showLoading.postValue(true)
        val valid = checkDataValidation()
        if (valid != VALID_DATA) _response.postValue(valid)
        else {
            viewModelScope.launch {
                createQuiz(
                    Quiz(
                        image = convertImageToByteArray(
                            app.contentResolver,
                            quizPlaceholder.value!!.toUri()
                        )!!,
                        creatorUid = currentUserUID,
                        title = "${quizAcademicSubject.value} Quiz For ${quizAcademicYear.value} Students",
                        subject = quizAcademicSubject.value!!,
                        academicYear = quizAcademicYear.value!!,
                        duration = _quizDurationLong.value!!,
                        description = quizDescription.value!!.m391Capitalize()
                    )
                )
                _response.postValue(RESPONSE_SUCCESS)
            }
        }
    }

    private fun convertImageToByteArray(
        contentResolver: ContentResolver,
        imageUri: Uri
    ): ByteArray? {
        return try {
            val inputStream = contentResolver.openInputStream(imageUri)
            val bitmap = inputStream?.use { BitmapFactory.decodeStream(it) }
            val outputStream = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.toByteArray()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun checkDataValidation(): String {
        return if (quizPlaceholder.value.isNullOrEmpty()) EMPTY_QUIZ_PLACEHOLDER_IMAGE
        else if (quizAcademicSubject.value.equals(app.getString(R.string.academic_subject))) EMPTY_QUIZ_ACADEMIC_SUBJECTS
        else if (quizAcademicYear.value.equals(app.getString(R.string.academic_year))) EMPTY_QUIZ_ACADEMIC_YEAR
        else if (quizDurationString.value.equals(app.getString(R.string.select_quiz_duration))) EMPTY_QUIZ_DURATION
        else if (quizDescription.value.isNullOrEmpty() || quizDescription.value.isNullOrBlank()) EMPTY_QUIZ_DESCRIPTION
        else VALID_DATA
    }
}