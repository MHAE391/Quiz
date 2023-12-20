package com.m391.quiz.ui.teacher.home

import com.m391.quiz.R
import com.m391.quiz.database.local.entities.Quiz
import com.m391.quiz.utils.BaseRecyclerViewAdapter

class QuizzesAdapter<T>(private val view: Int, private val callback: (T) -> Unit) :
    BaseRecyclerViewAdapter<T>(callback) {
    override fun getLayoutRes(viewType: Int) = view
}