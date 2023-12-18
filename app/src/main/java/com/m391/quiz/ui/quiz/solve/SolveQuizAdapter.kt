package com.m391.quiz.ui.quiz.solve

import com.m391.quiz.R
import com.m391.quiz.models.QuestionFirebaseModel
import com.m391.quiz.models.QuestionFirebaseUIModel
import com.m391.quiz.utils.BaseRecyclerViewAdapter

class SolveQuizAdapter(callback: (QuestionFirebaseUIModel) -> Unit) :
    BaseRecyclerViewAdapter<QuestionFirebaseUIModel>(callback) {
    override fun getLayoutRes(viewType: Int) = R.layout.solve_question_item
}