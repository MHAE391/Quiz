package com.m391.quiz.ui.quiz.preview

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.m391.quiz.database.local.entities.Question
import com.m391.quiz.database.local.entities.Quiz
import com.m391.quiz.database.remote.Quizzes
import com.m391.quiz.models.QuestionUIModel
import com.m391.quiz.ui.shared.BaseViewModel
import com.m391.quiz.utils.Statics.EMPTY_QUIZ_QUESTION
import com.m391.quiz.utils.m391RemoteModel
import com.m391.quiz.utils.m391UIModel
import kotlinx.coroutines.launch

class PreviewQuizViewModel(
    private val app: Application,
    private val quizId: String,
    private val getQuizById: suspend (quizId: String) -> Quiz?,
    private val getAllQuizQuestions: suspend (quizId: String) -> List<Question>,
    private val quizFirebase: Quizzes
) :
    BaseViewModel(app) {

    private val _quizQuestions = MutableLiveData<List<QuestionUIModel>>()
    private val _currentQuiz = MutableLiveData<Quiz>()

    val quizQuestions: LiveData<List<QuestionUIModel>> = _quizQuestions

    val currentQuiz: LiveData<Quiz> = _currentQuiz

    init {
        viewModelScope.launch {
            _currentQuiz.postValue(getQuizById(quizId))
        }
    }

    suspend fun refreshQuestions() {
        viewModelScope.launch {
            _quizQuestions.postValue(
                getAllQuizQuestions(quizId).m391UIModel()
            )
        }
    }

    private val _result = MutableLiveData<String>()
    val result: LiveData<String> = _result

    fun resetResult() {
        _result.postValue(String())
    }

    suspend fun uploadQuizToFirestore() {
        positiveShowLoading()
        if (quizQuestions.value.isNullOrEmpty()) _result.postValue(EMPTY_QUIZ_QUESTION)
        else {
            viewModelScope.launch {
                _result.postValue(
                    quizFirebase.uploadQuiz(
                        quiz = currentQuiz.value!!.m391RemoteModel(quizQuestions.value!!)
                    )
                )
            }
        }
    }
}