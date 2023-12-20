package com.m391.quiz.ui.quiz.marking.mark

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.m391.quiz.database.remote.Solutions
import com.m391.quiz.models.QuestionFirebaseUIModel
import com.m391.quiz.models.QuestionScore
import com.m391.quiz.models.QuizFirebaseModel
import com.m391.quiz.ui.shared.BaseViewModel
import com.m391.quiz.utils.m391FirebaseUIModel
import kotlinx.coroutines.launch

class MarkQuizViewModel(
    private val app: Application,
    private val quiz: QuizFirebaseModel,
    private val studentUid: String,
    private val solutions: Solutions
) :
    BaseViewModel(app) {

    private val _questions = MutableLiveData<List<QuestionFirebaseUIModel>>()
    val questions: LiveData<List<QuestionFirebaseUIModel>> = _questions

    init {
        refreshQuizQuestions()
    }

    fun refreshQuizQuestions() {
        _questions.postValue(quiz.questions.m391FirebaseUIModel())

    }

    private val _studentScores = MutableLiveData<List<QuestionScore>>()
    val studentScores: LiveData<List<QuestionScore>> = _studentScores

    private val _studentTotalScore = MutableLiveData<Int>()

    val studentTotalScore: LiveData<Int> = _studentTotalScore

    suspend fun getStudentScores(lifecycleOwner: LifecycleOwner) {
        viewModelScope.launch {
            solutions.getStudentQuizScores(studentUid, quiz.quiz_id).observe(lifecycleOwner) {
                _studentScores.postValue(it)
                _studentTotalScore.postValue(it.m391Score())
            }
        }
    }

    suspend fun removeStudentScores(lifecycleOwner: LifecycleOwner) {
        viewModelScope.launch {
            solutions.getStudentQuizScores(studentUid, quiz.quiz_id).removeObservers(lifecycleOwner)
            solutions.closeSolutionsStream()
        }
    }

    private fun List<QuestionScore>.m391Score(): Int {
        var score = 0
        this.forEach {
            score += it.score ?: 0
        }
        return score
    }


}