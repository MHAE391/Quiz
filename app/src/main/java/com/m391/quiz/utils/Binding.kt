package com.m391.quiz.utils

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.m391.quiz.R
import com.squareup.picasso.Picasso

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

    @BindingAdapter(
        value = ["android:mcqVisibilityWithAnimation"]
    )
    @JvmStatic
    fun mcqVisibilityAnimation(view: View, visibility: Int) {
        if (visibility == View.GONE) view.visibility = visibility
        else {
            expandView(view, (1500).toLong())
        }
    }

    @BindingAdapter(
        value = ["android:essayVisibilityWithAnimation"]
    )
    @JvmStatic
    fun essayVisibilityAnimation(view: View, visibility: Int) {
        if (visibility == View.GONE) view.visibility = visibility
        else {
            expandView(view, (500).toLong())
        }
    }


    private fun expandView(view: View, duration: Long) {
        view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val targetHeight = view.measuredHeight
        view.visibility = View.GONE
        val animator = ValueAnimator.ofInt(0, targetHeight)
        animator.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Int
            view.layoutParams.height = animatedValue
            view.requestLayout()
        }
        animator.duration = duration
        animator.interpolator = DecelerateInterpolator()
        animator.start()

        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                view.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animator) {}
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
    }

    @BindingAdapter("android:questionImageByteArray")
    @JvmStatic
    fun loadQuestionImageByteArray(imageView: ImageView, image: ByteArray?) {
        if (image == null) imageView.visibility = View.GONE
        else {
            val bitmap = convertByteArrayToBitmap(image)
            imageView.setImageBitmap(bitmap)
        }
    }
}
