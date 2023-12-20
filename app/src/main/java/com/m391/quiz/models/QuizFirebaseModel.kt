package com.m391.quiz.models

import java.io.Serializable
import java.util.Date

data class QuizFirebaseModel(
    val quiz_id: String,
    val quiz_duration: Long,
    val quiz_creator: String,
    val quiz_image_url: String,
    val quiz_image_path: String,
    val quiz_academic_year: String,
    val quiz_subject: String,
    val quiz_creation_time: Date,
    val quiz_description: String,
    val quiz_title: String,
    val quiz_score: Int,
    val questions: List<QuestionFirebaseModel>,
    val studentTotalScore: Int? = null
) : Serializable

