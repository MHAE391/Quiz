package com.m391.quiz.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.View
import androidx.core.net.toUri
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.ByteArrayOutputStream
import java.io.IOException
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
    return this.lowercase(Locale.ROOT).trim().replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale.getDefault()
        )
        else it.toString()
    }
}

fun String?.m391Blank(): String {
    return if (this.isNullOrBlank()) return String()
    else this
}

private fun convertImageToByteArray(
    contentResolver: ContentResolver,
    imageUri: Uri
): ByteArray? {
    return try {
        val inputStream = contentResolver.openInputStream(imageUri)
        val bitmap = inputStream?.use { BitmapFactory.decodeStream(it) }
        val outputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.toByteArray()
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

fun String?.m391ByteArray(contentResolver: ContentResolver): ByteArray? {
    return if (this.isNullOrBlank()) return null
    else convertImageToByteArray(contentResolver, this.toUri())
}