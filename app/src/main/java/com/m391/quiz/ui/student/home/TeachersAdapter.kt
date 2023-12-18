package com.m391.quiz.ui.student.home

import com.m391.quiz.R
import com.m391.quiz.models.TeacherFirebaseModel
import com.m391.quiz.utils.BaseRecyclerViewAdapter

class TeachersAdapter(callbacks: (TeacherFirebaseModel) -> Unit) :
    BaseRecyclerViewAdapter<TeacherFirebaseModel>(callbacks) {
    override fun getLayoutRes(viewType: Int) = R.layout.teacher_iteam
}