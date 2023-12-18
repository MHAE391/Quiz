package com.m391.quiz.utils

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.m391.quiz.BR
import com.m391.quiz.database.local.entities.Quiz
import com.m391.quiz.models.CheckItem
import com.m391.quiz.models.QuestionFirebaseModel
import com.m391.quiz.models.QuestionFirebaseUIModel
import com.m391.quiz.models.QuestionUIModel
import com.m391.quiz.models.QuizFirebaseModel
import com.m391.quiz.models.TeacherFirebaseModel


class DataBindingViewHolder<T>(private val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: T) {
        when (item) {
            is CheckItem -> {
                binding.setVariable(BR.checkItem, item)
            }

            is Quiz -> {
                binding.setVariable(BR.quiz, item)
            }

            is QuestionUIModel -> {
                binding.setVariable(BR.question, item)
            }

            is TeacherFirebaseModel -> {
                binding.setVariable(BR.teacher, item)
            }

            is QuizFirebaseModel -> {
                binding.setVariable(BR.quiz, item)
            }

            is QuestionFirebaseUIModel -> {
                binding.setVariable(BR.question, item)
            }
        }
        binding.executePendingBindings()
    }
}