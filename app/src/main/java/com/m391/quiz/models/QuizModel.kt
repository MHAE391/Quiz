package com.m391.quiz.models

import java.util.Date

data class QuizModel(
    val id: String,
    val duration: Long,
    val creatorUid: String,
    val image: ByteArray,
    val academicYear: String,
    val subject: String,
    val creationTime: Date,
    val description: String,
    val title: String,
    val questions: List<QuestionUIModel>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QuizModel

        return image.contentEquals(other.image)
    }

    override fun hashCode(): Int {
        return image.contentHashCode()
    }
}