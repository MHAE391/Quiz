package com.m391.quiz.utils

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.m391.quiz.BR
import com.m391.quiz.database.local.entities.Question
import com.m391.quiz.database.local.entities.Quiz
import com.m391.quiz.models.CheckItem


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

            is Question -> {
                binding.setVariable(BR.question, item)
            }
        }
        binding.executePendingBindings()
    }
}