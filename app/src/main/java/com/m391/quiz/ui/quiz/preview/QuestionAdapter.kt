package com.m391.quiz.ui.quiz.preview

import com.m391.quiz.R
import com.m391.quiz.database.local.entities.Question
import com.m391.quiz.utils.BaseRecyclerViewAdapter

class QuestionAdapter(callback: (Question) -> Unit) : BaseRecyclerViewAdapter<Question>(callback) {
    override fun getLayoutRes(viewType: Int): Int = R.layout.question_item

}