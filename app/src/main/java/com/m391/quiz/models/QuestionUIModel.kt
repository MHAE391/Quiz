package com.m391.quiz.models

import java.io.Serializable


data class QuestionUIModel(
    val id: Int,
    val quizId: String,
    val questionHeaderText: String,
    val questionBodyText: String?,
    val questionBodyImage: ByteArray?,
    val answerType: String,
    val answerBodyText: Boolean,
    val answerBodyImage: Boolean,
    val answerFirstChoiceText: String?,
    val answerFirstChoiceImage: ByteArray?,
    val answerSecondChoiceText: String?,
    val answerSecondChoiceImage: ByteArray?,
    val answerThirdChoiceText: String?,
    val answerThirdChoiceImage: ByteArray?,
    val answerFourthChoiceText: String?,
    val answerFourthChoiceImage: ByteArray?,
    val questionNumber: Int = 0,
    val questionScore: Int
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QuestionUIModel

        if (questionBodyImage != null) {
            if (other.questionBodyImage == null) return false
            if (!questionBodyImage.contentEquals(other.questionBodyImage)) return false
        } else if (other.questionBodyImage != null) return false
        if (answerFirstChoiceImage != null) {
            if (other.answerFirstChoiceImage == null) return false
            if (!answerFirstChoiceImage.contentEquals(other.answerFirstChoiceImage)) return false
        } else if (other.answerFirstChoiceImage != null) return false
        if (answerSecondChoiceImage != null) {
            if (other.answerSecondChoiceImage == null) return false
            if (!answerSecondChoiceImage.contentEquals(other.answerSecondChoiceImage)) return false
        } else if (other.answerSecondChoiceImage != null) return false
        if (answerThirdChoiceImage != null) {
            if (other.answerThirdChoiceImage == null) return false
            if (!answerThirdChoiceImage.contentEquals(other.answerThirdChoiceImage)) return false
        } else if (other.answerThirdChoiceImage != null) return false
        if (answerFourthChoiceImage != null) {
            if (other.answerFourthChoiceImage == null) return false
            if (!answerFourthChoiceImage.contentEquals(other.answerFourthChoiceImage)) return false
        } else if (other.answerFourthChoiceImage != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = questionBodyImage?.contentHashCode() ?: 0
        result = 31 * result + (answerFirstChoiceImage?.contentHashCode() ?: 0)
        result = 31 * result + (answerSecondChoiceImage?.contentHashCode() ?: 0)
        result = 31 * result + (answerThirdChoiceImage?.contentHashCode() ?: 0)
        result = 31 * result + (answerFourthChoiceImage?.contentHashCode() ?: 0)
        return result
    }

}