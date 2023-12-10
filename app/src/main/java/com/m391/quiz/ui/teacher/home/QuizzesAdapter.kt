package com.m391.quiz.ui.teacher.home

import com.m391.quiz.R
import com.m391.quiz.database.local.entities.Quiz
import com.m391.quiz.utils.BaseRecyclerViewAdapter

class QuizzesAdapter(private val callback: (Quiz) -> Unit) :
    BaseRecyclerViewAdapter<Quiz>(callback) {
    override fun getLayoutRes(viewType: Int) = R.layout.quiz_item
}