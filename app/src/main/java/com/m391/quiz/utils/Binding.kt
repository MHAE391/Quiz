package com.m391.quiz.utils

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.media.MediaMetadataRetriever
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.time.Duration

object Binding {
    @Suppress("UNCHECKED_CAST")
    @BindingAdapter("android:liveData")
    @JvmStatic
    fun <T> setRecyclerViewData(recyclerView: RecyclerView, items: LiveData<List<T>>?) {
        items?.value?.let { itemList ->
            (recyclerView.adapter as? BaseRecyclerViewAdapter<T>)?.apply {
                clear()
                addData(itemList)
                recyclerView.scrollToPosition(0)
            }
        }
    }

    @BindingAdapter("android:imageUrl")
    @JvmStatic
    fun loadImage(imageView: ImageView, imageUrl: String) {
        val circularProgressDrawable = CircularProgressDrawable(imageView.context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.setColorSchemeColors(Color.TRANSPARENT)
        circularProgressDrawable.start()
        Picasso.get().load(imageUrl)
            .placeholder(circularProgressDrawable)
            .into(imageView)
    }
}
