package com.m391.quiz.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

fun <T> RecyclerView.setupGridRecycler(
    adapter: BaseRecyclerViewAdapter<T>
) {
    this.apply {
        layoutManager = GridLayoutManager(this.context, 3)
        this.adapter = adapter
    }
}

fun <T> RecyclerView.setupLinearRecycler(
    adapter: BaseRecyclerViewAdapter<T>,
    verticalOrHorizontal: Boolean
) {
    this.apply {
        layoutManager =
            if (verticalOrHorizontal)
                LinearLayoutManager(this.context)
            else LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        this.adapter = adapter
    }
}

fun View.fadeIn() {
    this.visibility = View.VISIBLE
    this.alpha = 0f
    this.animate().alpha(1f).setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            this@fadeIn.alpha = 1f
        }
    })
}

fun View.fadeOut() {
    this.animate().alpha(0f).setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            this@fadeOut.alpha = 1f
            this@fadeOut.visibility = View.GONE
        }
    })
}

fun String.m391Capitalize(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale.getDefault()
        ).trim()
        else it.toString().trim()
    }
}