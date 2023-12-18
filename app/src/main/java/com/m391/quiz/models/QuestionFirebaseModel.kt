package com.m391.quiz.models

import java.io.Serializable

data class QuestionFirebaseModel(
    val quiz_id: String,
    val question_header_text: String,
    val question_body_text: String?,
    val question_body_image_url: String?,
    val question_body_image_path: String?,
    val answer_type: String,
    val answer_body_text: Boolean,
    val answer_body_image: Boolean,
    val answer_first_choice_text: String?,
    val answer_first_choice_image_url: String?,
    val answer_first_choice_image_path: String?,
    val answer_second_choice_text: String?,
    val answer_second_choice_image_url: String?,
    val answer_second_choice_image_path: String?,
    val answer_third_choice_text: String?,
    val answer_third_choice_image_url: String?,
    val answer_third_choice_image_path: String?,
    val answer_fourth_choice_text: String?,
    val answer_fourth_choice_image_url: String?,
    val answer_fourth_choice_image_path: String?,
    val question_score: Int
) : Serializable
