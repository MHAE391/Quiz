package com.m391.quiz.models

import java.io.Serializable

data class QuestionScore(
    val questionId: String?,
    val score: Int?,
    val comment: String?
) : Serializable