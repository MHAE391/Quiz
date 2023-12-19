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
import com.m391.quiz.database.local.entities.Question
import com.m391.quiz.database.local.entities.Quiz
import com.m391.quiz.models.QuestionFirebaseModel
import com.m391.quiz.models.QuestionFirebaseUIModel
import com.m391.quiz.models.QuestionUIModel
import com.m391.quiz.models.QuizModel
import com.m391.quiz.utils.Statics.ANSWER_BODY_IMAGE
import com.m391.quiz.utils.Statics.ANSWER_BODY_TEXT
import com.m391.quiz.utils.Statics.ANSWER_FIRST_CHOICE_IMAGE_PATH
import com.m391.quiz.utils.Statics.ANSWER_FIRST_CHOICE_IMAGE_URL
import com.m391.quiz.utils.Statics.ANSWER_FIRST_CHOICE_TEXT
import com.m391.quiz.utils.Statics.ANSWER_FOURTH_CHOICE_IMAGE_PATH
import com.m391.quiz.utils.Statics.ANSWER_FOURTH_CHOICE_IMAGE_URL
import com.m391.quiz.utils.Statics.ANSWER_FOURTH_CHOICE_TEXT
import com.m391.quiz.utils.Statics.ANSWER_SECOND_CHOICE_IMAGE_PATH
import com.m391.quiz.utils.Statics.ANSWER_SECOND_CHOICE_IMAGE_URL
import com.m391.quiz.utils.Statics.ANSWER_SECOND_CHOICE_TEXT
import com.m391.quiz.utils.Statics.ANSWER_THIRD_CHOICE_IMAGE_PATH
import com.m391.quiz.utils.Statics.ANSWER_THIRD_CHOICE_IMAGE_URL
import com.m391.quiz.utils.Statics.ANSWER_THIRD_CHOICE_TEXT
import com.m391.quiz.utils.Statics.ANSWER_TYPE
import com.m391.quiz.utils.Statics.QUESTION_BODY_IMAGE_PATH
import com.m391.quiz.utils.Statics.QUESTION_BODY_IMAGE_URL
import com.m391.quiz.utils.Statics.QUESTION_BODY_TEXT
import com.m391.quiz.utils.Statics.QUESTION_HEADER_TEXT
import com.m391.quiz.utils.Statics.QUESTION_ID
import com.m391.quiz.utils.Statics.QUESTION_SCORE
import com.m391.quiz.utils.Statics.QUIZ_ID
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.HashMap
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

fun List<Question>.m391UIModel(): List<QuestionUIModel> {
    var number = 0
    return this.map { question ->
        question.m391UIModel(++number)
    }
}

fun Question.m391UIModel(number: Int): QuestionUIModel {
    return QuestionUIModel(
        quizId = this.quizId,
        id = this.id,
        questionNumber = number,
        questionScore = this.questionScore,
        questionBodyText = this.bodyText,
        questionBodyImage = this.bodyImage,
        questionHeaderText = this.headerText,
        answerType = this.answerType,
        answerBodyImage = this.answerBodyImage,
        answerBodyText = this.answerBodyText,
        answerFirstChoiceImage = this.answerChoicesFirstImage,
        answerFirstChoiceText = this.answerChoicesFirstText,
        answerSecondChoiceImage = this.answerChoicesSecondImage,
        answerSecondChoiceText = this.answerChoicesSecondText,
        answerThirdChoiceImage = this.answerChoicesThirdImage,
        answerThirdChoiceText = this.answerChoicesThirdText,
        answerFourthChoiceImage = this.answerChoicesFourthImage,
        answerFourthChoiceText = this.answerChoicesFourthText
    )
}

fun QuestionUIModel.m391DatabaseModel(): Question {
    return Question(
        id = this.id,
        quizId = this.quizId,
        answerBodyText = this.answerBodyText,
        answerBodyImage = this.answerBodyImage,
        answerType = this.answerType,
        headerText = this.questionHeaderText,
        bodyText = this.questionBodyText,
        bodyImage = this.questionBodyImage,
        questionScore = this.questionScore,
        answerChoicesFourthImage = this.answerFourthChoiceImage,
        answerChoicesFourthText = this.answerFourthChoiceText,
        answerChoicesThirdImage = this.answerThirdChoiceImage,
        answerChoicesThirdText = this.answerThirdChoiceText,
        answerChoicesSecondImage = this.answerSecondChoiceImage,
        answerChoicesSecondText = this.answerSecondChoiceText,
        answerChoicesFirstText = this.answerFirstChoiceText,
        answerChoicesFirstImage = this.answerFirstChoiceImage
    )
}

fun Quiz.m391RemoteModel(questions: List<QuestionUIModel>): QuizModel {
    return QuizModel(
        questions = questions,
        id = this.id,
        title = this.title,
        description = this.description,
        academicYear = this.academicYear,
        subject = this.subject,
        duration = this.duration,
        creatorUid = this.creatorUid,
        creationTime = this.creationTime,
        image = this.image
    )
}

fun QuestionUIModel.m391FirebaseModel(
    bodyImageUrl: String?,
    bodyImagePath: String?,
    firstChoiceImageUrl: String?,
    firstChoiceImagePath: String?,
    secondChoiceImageUrl: String?,
    secondChoiceImagePath: String?,
    thirdChoiceImageUrl: String?,
    thirdChoiceImagePath: String?,
    fourthChoiceImageUrl: String?,
    fourthChoiceImagePath: String?
): QuestionFirebaseModel {
    return QuestionFirebaseModel(
        question_id = this.id,
        quiz_id = this.quizId,
        answer_body_image = this.answerBodyImage,
        answer_body_text = this.answerBodyText,
        question_score = this.questionScore,
        question_body_text = this.questionBodyText,
        question_header_text = this.questionHeaderText,
        question_body_image_path = bodyImagePath,
        question_body_image_url = bodyImageUrl,
        answer_type = this.answerType,
        answer_first_choice_image_path = firstChoiceImagePath,
        answer_first_choice_image_url = firstChoiceImageUrl,
        answer_second_choice_image_path = secondChoiceImagePath,
        answer_second_choice_image_url = secondChoiceImageUrl,
        answer_third_choice_image_path = thirdChoiceImagePath,
        answer_third_choice_image_url = thirdChoiceImageUrl,
        answer_fourth_choice_image_path = fourthChoiceImagePath,
        answer_fourth_choice_image_url = fourthChoiceImageUrl,
        answer_first_choice_text = this.answerFirstChoiceText,
        answer_second_choice_text = this.answerSecondChoiceText,
        answer_third_choice_text = this.answerThirdChoiceText,
        answer_fourth_choice_text = this.answerFourthChoiceText
    )
}

fun List<QuestionUIModel>.m391Score(): Int {
    var score = 0
    this.forEach { question ->
        score += question.questionScore
    }
    return score
}

fun QuestionFirebaseUIModel.m391FirebaseModel(): QuestionFirebaseModel {
    return QuestionFirebaseModel(
        question_id = this.questionId,
        quiz_id = this.quizId,
        answer_body_image = this.answerBodyImage,
        answer_body_text = this.answerBodyText,
        question_score = this.questionScore,
        question_body_text = this.questionBodyText,
        question_header_text = this.questionHeaderText,
        question_body_image_path = this.questionBodyImagePath,
        question_body_image_url = this.questionBodyImageUrl,
        answer_type = this.answerType,
        answer_first_choice_image_path = this.answerFirstChoiceImagePath,
        answer_first_choice_image_url = this.answerFirstChoiceImageUrl,
        answer_second_choice_image_path = this.answerSecondChoiceImagePath,
        answer_second_choice_image_url = this.answerSecondChoiceImageUrl,
        answer_third_choice_image_path = this.answerThirdChoiceImagePath,
        answer_third_choice_image_url = this.answerThirdChoiceImageUrl,
        answer_fourth_choice_image_path = this.answerFourthChoiceImagePath,
        answer_fourth_choice_image_url = this.answerFourthChoiceImageUrl,
        answer_first_choice_text = this.answerFirstChoiceText,
        answer_second_choice_text = this.answerSecondChoiceText,
        answer_third_choice_text = this.answerThirdChoiceText,
        answer_fourth_choice_text = this.answerFourthChoiceText
    )
}

fun String?.m391ByteArray(contentResolver: ContentResolver): ByteArray? {
    return if (this.isNullOrBlank()) return null
    else convertImageToByteArray(contentResolver, this.toUri())
}

fun List<HashMap<String, Any>>.m391List(): List<QuestionFirebaseModel> {
    return this.map { question ->
        QuestionFirebaseModel(
            question_id = question[QUESTION_ID].toString(),
            quiz_id = question[QUIZ_ID].toString(),
            question_score = question[QUESTION_SCORE].toString().toInt(),
            question_body_image_path = question[QUESTION_BODY_IMAGE_PATH].toString(),
            question_body_image_url = question[QUESTION_BODY_IMAGE_URL].toString(),
            question_body_text = question[QUESTION_BODY_TEXT].toString(),
            question_header_text = question[QUESTION_HEADER_TEXT].toString(),
            answer_type = question[ANSWER_TYPE].toString(),
            answer_body_text = question[ANSWER_BODY_TEXT].toString().toBoolean(),
            answer_body_image = question[ANSWER_BODY_IMAGE].toString().toBoolean(),
            answer_first_choice_text = question[ANSWER_FIRST_CHOICE_TEXT].toString(),
            answer_first_choice_image_path = question[ANSWER_FIRST_CHOICE_IMAGE_PATH].toString(),
            answer_first_choice_image_url = question[ANSWER_FIRST_CHOICE_IMAGE_URL].toString(),
            answer_second_choice_text = question[ANSWER_SECOND_CHOICE_TEXT].toString(),
            answer_second_choice_image_path = question[ANSWER_SECOND_CHOICE_IMAGE_PATH].toString(),
            answer_second_choice_image_url = question[ANSWER_SECOND_CHOICE_IMAGE_URL].toString(),
            answer_third_choice_text = question[ANSWER_THIRD_CHOICE_TEXT].toString(),
            answer_third_choice_image_path = question[ANSWER_THIRD_CHOICE_IMAGE_PATH].toString(),
            answer_third_choice_image_url = question[ANSWER_THIRD_CHOICE_IMAGE_URL].toString(),
            answer_fourth_choice_text = question[ANSWER_FOURTH_CHOICE_TEXT].toString(),
            answer_fourth_choice_image_path = question[ANSWER_FOURTH_CHOICE_IMAGE_PATH].toString(),
            answer_fourth_choice_image_url = question[ANSWER_FOURTH_CHOICE_IMAGE_URL].toString(),
        )
    }
}

fun List<QuestionFirebaseModel>.m391FirebaseUIModel(): List<QuestionFirebaseUIModel> {
    var number = 0
    return this.map { question ->
        QuestionFirebaseUIModel(
            questionId = question.question_id,
            questionNumber = ++number,
            quizId = question.quiz_id,
            questionScore = question.question_score,
            questionBodyImagePath = question.question_body_image_path,
            questionBodyImageUrl = question.question_body_image_url,
            questionBodyText = question.question_body_text,
            questionHeaderText = question.question_header_text,
            answerType = question.answer_type,
            answerBodyText = question.answer_body_text,
            answerBodyImage = question.answer_body_image,
            answerFirstChoiceText = question.answer_first_choice_text,
            answerFirstChoiceImagePath = question.answer_first_choice_image_path,
            answerFirstChoiceImageUrl = question.answer_first_choice_image_url,
            answerSecondChoiceText = question.answer_second_choice_text,
            answerSecondChoiceImagePath = question.answer_second_choice_image_path,
            answerSecondChoiceImageUrl = question.answer_second_choice_image_url,
            answerThirdChoiceText = question.answer_third_choice_text,
            answerThirdChoiceImagePath = question.answer_third_choice_image_path,
            answerThirdChoiceImageUrl = question.answer_third_choice_image_url,
            answerFourthChoiceText = question.answer_fourth_choice_text,
            answerFourthChoiceImagePath = question.answer_fourth_choice_image_path,
            answerFourthChoiceImageUrl = question.answer_fourth_choice_image_url
        )
    }
}
/*
const val QUESTION_HEADER_TEXT = "question_header_text"
    const val QUESTION_BODY_TEXT = "question_body_text"
    const val QUESTION_BODY_IMAGE_URL = "question_body_image_url"
    const val QUESTION_BODY_IMAGE_PATH = "question_body_image_path"
    const val ANSWER_TYPE = "answer_type"
    const val ANSWER_BODY_TEXT = "answer_body_text"
    const val ANSWER_BODY_IMAGE = "answer_body_image"
    const val ANSWER_FIRST_CHOICE_TEXT = "answer_first_choice_text"
    const val ANSWER_FIRST_CHOICE_IMAGE_URL = "answer_first_choice_image_url"
    const val ANSWER_FIRST_CHOICE_IMAGE_PATH = "answer_first_choice_image_path"

    const val ANSWER_SECOND_CHOICE_TEXT = "answer_second_choice_text"
    const val ANSWER_SECOND_CHOICE_IMAGE_URL = "answer_second_choice_image_url"
    const val ANSWER_SECOND_CHOICE_IMAGE_PATH = "answer_second_choice_image_path"

    const val ANSWER_THIRD_CHOICE_TEXT = "answer_third_choice_text"
    const val ANSWER_THIRD_CHOICE_IMAGE_URL = "answer_third_choice_image_url"
    const val ANSWER_THIRD_CHOICE_IMAGE_PATH = "answer_third_choice_image_path"

    const val ANSWER_FOURTH_CHOICE_TEXT = "answer_fourth_choice_text"
    const val ANSWER_FOURTH_CHOICE_IMAGE_URL = "answer_fourth_choice_image_url"
    const val ANSWER_FOURTH_CHOICE_IMAGE_PATH = "answer_fourth_choice_image_path"

    const val QUESTION_SCORE = "question_score"
 */