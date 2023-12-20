package com.m391.quiz.utils

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.text.Editable
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.m391.quiz.R
import com.m391.quiz.database.local.entities.Quiz
import com.m391.quiz.models.QuizFirebaseModel
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.max
import kotlin.math.min

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

    @BindingAdapter("android:items_list")
    @JvmStatic
    fun loadItems(textView: TextView, items: List<String>) {
        var itemsString = String()
        items.forEach { item ->
            itemsString += "- $item"
            if (items.indexOf(item) != items.size - 1) itemsString += "\n"
        }
        textView.text = itemsString
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

    @SuppressLint("SetTextI18n")
    @BindingAdapter("android:question_score_points")
    @JvmStatic
    fun loadStudentScorePoints(textView: TextView, score: Int?) {
        if (score == null) textView.visibility = View.GONE
        else {
            textView.visibility = View.VISIBLE
            textView.text = "$score Points"
        }
    }

    @SuppressLint("SetTextI18n")
    @BindingAdapter("android:question_score")
    @JvmStatic
    fun loadStudentScore(textView: EditText, score: Int?) {
        if (score != null) textView.setText(score.toString())
    }

    @BindingAdapter("android:answer_comment_grade")
    @JvmStatic
    fun setAnswerComment(textView: TextView, comment: String?) {
        if (comment == null) textView.visibility = View.GONE
        else {
            textView.visibility = View.VISIBLE
            textView.text = comment
        }
    }

    @SuppressLint("SetTextI18n")
    @BindingAdapter("android:answer_comment")
    @JvmStatic
    fun loadStudentAnswerComment(textView: EditText, comment: String?) {
        try {
            if (comment != null) textView.setText(comment)
        } catch (e: Exception) {
            Log.e("asdassadaa", e.localizedMessage!!)
        }
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
        val targetHeight =
            if (duration == 500L) view.measuredHeight else 1850
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
            imageView.visibility = View.VISIBLE
            val bitmap = convertByteArrayToBitmap(image)
            imageView.setImageBitmap(bitmap)
        }
    }

    @BindingAdapter("android:questionImageUrl")
    @JvmStatic
    fun loadQuestionImageUrl(imageView: ImageView, image: String?) {
        if (image.isNullOrBlank()) imageView.visibility = View.GONE
        else {
            imageView.visibility = View.VISIBLE
            loadPlaceholderImage(imageView, image)
        }
    }

    @SuppressLint("SimpleDateFormat")
    @BindingAdapter("android:date")
    @JvmStatic
    fun setDate(textView: TextView, date: Date) {
        val sdf = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
        val time = sdf.format(date)
        textView.text = time
    }

    @SuppressLint("SimpleDateFormat")
    @BindingAdapter("android:time")
    @JvmStatic
    fun convertMillisecondsToTime(textView: TextView, ms: Long) {
        val totalSeconds = ms / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        textView.text = String.format("%02d:%02d", hours, minutes)
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    @BindingAdapter("android:time_progress")
    @JvmStatic
    fun convertMillisecondsToTimeProgress(textView: TextView, ms: Long) {
        val seconds = (ms / 1000)
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val remainingSeconds = seconds % 60

        val formattedHours = if (hours > 0) "$hours:" else ""
        val formattedMinutes = String.format("%02d", minutes)
        val formattedSeconds = String.format("%02d", remainingSeconds)
        textView.text = "$formattedHours$formattedMinutes:$formattedSeconds"
    }
}
