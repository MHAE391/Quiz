package com.m391.quiz.database.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "questions",
    foreignKeys = [ForeignKey(
        entity = Quiz::class,
        parentColumns = ["quiz_id"],
        childColumns = ["quiz_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Question(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "question_id") val id: Int = 0,
    @ColumnInfo(name = "quiz_id") val quizId: String,
    @ColumnInfo(name = "question_header_text") val headerText: String,
    @ColumnInfo(name = "question_body_text") val bodyText: String?,
    @ColumnInfo(name = "question_body_image") val bodyImage: ByteArray?,
    @ColumnInfo(name = "answer_type") val answerType: String,
    @ColumnInfo(name = "answer_body_text") val answerBodyText: Boolean,
    @ColumnInfo(name = "answer_body_image") val answerBodyImage: Boolean,
    @ColumnInfo(name = "answer_choices_first_text") val answerChoicesFirstText: String?,
    @ColumnInfo(name = "answer_choices_first_image") val answerChoicesFirstImage: ByteArray?,
    @ColumnInfo(name = "answer_choices_second_text") val answerChoicesSecondText: String?,
    @ColumnInfo(name = "answer_choices_second_image") val answerChoicesSecondImage: ByteArray?,
    @ColumnInfo(name = "answer_choices_third_text") val answerChoicesThirdText: String?,
    @ColumnInfo(name = "answer_choices_third_image") val answerChoicesThirdImage: ByteArray?,
    @ColumnInfo(name = "answer_choices_fourth_text") val answerChoicesFourthText: String?,
    @ColumnInfo(name = "answer_choices_fourth_image") val answerChoicesFourthImage: ByteArray?,
    @ColumnInfo(name = "question_score") val questionScore: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Question

        if (bodyImage != null) {
            if (other.bodyImage == null) return false
            if (!bodyImage.contentEquals(other.bodyImage)) return false
        } else if (other.bodyImage != null) return false
        if (answerChoicesFirstImage != null) {
            if (other.answerChoicesFirstImage == null) return false
            if (!answerChoicesFirstImage.contentEquals(other.answerChoicesFirstImage)) return false
        } else if (other.answerChoicesFirstImage != null) return false
        if (answerChoicesSecondImage != null) {
            if (other.answerChoicesSecondImage == null) return false
            if (!answerChoicesSecondImage.contentEquals(other.answerChoicesSecondImage)) return false
        } else if (other.answerChoicesSecondImage != null) return false
        if (answerChoicesThirdImage != null) {
            if (other.answerChoicesThirdImage == null) return false
            if (!answerChoicesThirdImage.contentEquals(other.answerChoicesThirdImage)) return false
        } else if (other.answerChoicesThirdImage != null) return false
        if (answerChoicesFourthImage != null) {
            if (other.answerChoicesFourthImage == null) return false
            if (!answerChoicesFourthImage.contentEquals(other.answerChoicesFourthImage)) return false
        } else if (other.answerChoicesFourthImage != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bodyImage?.contentHashCode() ?: 0
        result = 31 * result + (answerChoicesFirstImage?.contentHashCode() ?: 0)
        result = 31 * result + (answerChoicesSecondImage?.contentHashCode() ?: 0)
        result = 31 * result + (answerChoicesThirdImage?.contentHashCode() ?: 0)
        result = 31 * result + (answerChoicesFourthImage?.contentHashCode() ?: 0)
        return result
    }
}
