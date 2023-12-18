package com.m391.quiz.ui.question.solve

import android.app.Application
import com.m391.quiz.models.QuestionFirebaseUIModel
import com.m391.quiz.ui.shared.BaseViewModel

class SolveQuestionViewModel(
    private val app: Application,
    val question: QuestionFirebaseUIModel
) : BaseViewModel(app) {
}