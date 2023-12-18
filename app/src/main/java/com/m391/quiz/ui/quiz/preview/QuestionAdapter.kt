package com.m391.quiz.ui.quiz.preview

import com.m391.quiz.R
import com.m391.quiz.models.QuestionUIModel
import com.m391.quiz.utils.BaseRecyclerViewAdapter

class QuestionAdapter(callback: (QuestionUIModel) -> Unit) : BaseRecyclerViewAdapter<QuestionUIModel>(callback) {
    override fun getLayoutRes(viewType: Int): Int = R.layout.question_item

}