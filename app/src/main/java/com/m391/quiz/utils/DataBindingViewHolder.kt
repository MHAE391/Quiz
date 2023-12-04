package com.m391.quiz.utils

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView


class DataBindingViewHolder<T>(private val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: T) {
        when (item) {

        }
        binding.executePendingBindings()
    }
}