package com.m391.quiz.ui.quiz.marking.students

import com.m391.quiz.R
import com.m391.quiz.models.StudentFirebaseModel
import com.m391.quiz.utils.BaseRecyclerViewAdapter

class StudentsAdapter(callback: (StudentFirebaseModel) -> Unit) :
    BaseRecyclerViewAdapter<StudentFirebaseModel>(callback) {
    override fun getLayoutRes(viewType: Int): Int = R.layout.student_item
}