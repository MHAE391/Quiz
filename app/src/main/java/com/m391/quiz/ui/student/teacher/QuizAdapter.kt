package com.m391.quiz.ui.student.teacher

import com.m391.quiz.R
import com.m391.quiz.models.QuizFirebaseModel
import com.m391.quiz.utils.BaseRecyclerViewAdapter

class QuizAdapter(callback: (QuizFirebaseModel) -> Unit) :
    BaseRecyclerViewAdapter<QuizFirebaseModel>(callback) {
    override fun getLayoutRes(viewType: Int): Int = R.layout.firebase_quiz_item
}