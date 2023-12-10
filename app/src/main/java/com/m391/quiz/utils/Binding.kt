package com.m391.quiz.utils

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.m391.quiz.R
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

    @BindingAdapter("android:fadeVisible")
    @JvmStatic
    fun setFadeVisible(view: View, visible: Boolean? = true) {
        if (view.tag == null) {
            view.tag = true
            view.visibility = if (visible == true) View.VISIBLE else View.GONE
        } else {
            view.animate().cancel()
            if (visible == true) {
                if (view.visibility == View.GONE)
                    view.fadeIn()
            } else {
                if (view.visibility == View.VISIBLE)
                    view.fadeOut()
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
        Picasso.get().load(imageUrl.toUri())
            .placeholder(circularProgressDrawable)
            .into(imageView)
    }

    @BindingAdapter("android:profileImage")
    @JvmStatic
    fun loadProfileImage(imageView: ImageView, imageUrl: String?) {
        if (imageUrl == null)
            imageView.setImageResource(R.mipmap.person)
        else loadImage(imageView, imageUrl)
    }

    @BindingAdapter("android:placeholderImage")
    @JvmStatic
    fun loadPlaceholderImage(imageView: ImageView, imageUrl: String?) {
        if (imageUrl == null)
            imageView.setImageResource(R.mipmap.quiz_placeholder)
        else loadImage(imageView, imageUrl)
    }

    @BindingAdapter("android:placeholderImageByteArray")
    @JvmStatic
    fun loadPlaceholderImageByteArray(imageView: ImageView, imageUrl: ByteArray) {
        val image = convertByteArrayToBitmap(imageUrl)
        imageView.setImageBitmap(image)
    }

    private fun convertByteArrayToBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}
