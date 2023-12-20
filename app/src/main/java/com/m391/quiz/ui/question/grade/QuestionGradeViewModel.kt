package com.m391.quiz.ui.question.grade

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.m391.quiz.database.remote.Solutions
import com.m391.quiz.models.QuestionFirebaseUIModel
import com.m391.quiz.models.QuestionScore
import com.m391.quiz.models.SolutionFirebaseModel
import com.m391.quiz.ui.shared.BaseViewModel
import com.m391.quiz.utils.Statics
import kotlinx.coroutines.launch

class QuestionGradeViewModel(
    private val app: Application,
    private val questionData: QuestionFirebaseUIModel,
    private val solutions: Solutions,
    private val studentId: String
) : BaseViewModel(app) {
    private val _question = MutableLiveData<QuestionFirebaseUIModel>()
    val question: LiveData<QuestionFirebaseUIModel> = _question

    private val _studentSolution = MutableLiveData<SolutionFirebaseModel>()
    val studentSolution: LiveData<SolutionFirebaseModel> = _studentSolution

    private val _questionScore = MutableLiveData<QuestionScore?>()
    val questionScore: LiveData<QuestionScore?> = _questionScore


    val firstMCQChecked = MutableLiveData<Boolean>()
    val secondMCQChecked = MutableLiveData<Boolean>()
    val thirdMCQChecked = MutableLiveData<Boolean>()
    val fourthMCQChecked = MutableLiveData<Boolean>()

    val answerScore = MutableLiveData<String>()
    val answerComment = MutableLiveData<String>()

    suspend fun studentSolution(lifecycleOwner: LifecycleOwner) {
        viewModelScope.launch {
            solutions.getStudentSolution(studentId, questionData.questionId, questionData.quizId)
                .observe(
                    lifecycleOwner
                ) {
                    _studentSolution.postValue(it)
                    if (questionData.answerType == "MCQ") {
                        when (it.mcqAnswer) {
                            Statics.MCQ_FIRST -> firstMCQChecked.postValue(true)
                            Statics.MCQ_SECOND -> secondMCQChecked.postValue(true)
                            Statics.MCQ_THIRD -> thirdMCQChecked.postValue(true)
                            Statics.MCQ_FOURTH -> fourthMCQChecked.postValue(true)
                        }
                    }
                }
        }
    }

    suspend fun getQuestionScore(lifecycleOwner: LifecycleOwner) {
        viewModelScope.launch {
            solutions.getStudentQuestionScore(
                studentId,
                questionData.quizId,
                questionData.questionId
            ).observe(lifecycleOwner) {
                _questionScore.postValue(it)
            }
        }
    }

    fun setQuestionData() {
        _question.postValue(questionData)
    }

    suspend fun stopStudentSolution(lifecycleOwner: LifecycleOwner) {
        viewModelScope.launch {
            solutions.getStudentSolution(studentId, questionData.questionId, questionData.quizId)
                .removeObservers(lifecycleOwner)
            solutions.closeSolutionsStream()
        }
    }
}