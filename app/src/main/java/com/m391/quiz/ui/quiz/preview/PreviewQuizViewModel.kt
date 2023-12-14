package com.m391.quiz.ui.quiz.preview

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.m391.quiz.database.local.entities.Question
import com.m391.quiz.database.local.entities.Quiz
import com.m391.quiz.ui.shared.BaseViewModel
import kotlinx.coroutines.launch

class PreviewQuizViewModel(
    private val app: Application,
    private val quizId: Int,
    private val getQuizById: suspend (quizId: Int) -> Quiz?,
    private val getAllQuizQuestions: suspend (quizId: Int) -> List<Question>
) :
    BaseViewModel(app) {

    private val _quizQuestions = MutableLiveData<List<Question>>()
    private val _currentQuiz = MutableLiveData<Quiz>()

    val quizQuestions: LiveData<List<Question>> = _quizQuestions
    val currentQuiz: LiveData<Quiz> = _currentQuiz

    init {
        viewModelScope.launch {
            _currentQuiz.postValue(getQuizById(quizId))
        }

    }

    suspend fun refreshQuestions() {
        viewModelScope.launch {
            _quizQuestions.postValue(
                getAllQuizQuestions(quizId)
            )
        }
    }
}