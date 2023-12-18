package com.m391.quiz.database.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.firebase.firestore.FirebaseFirestore
import com.m391.quiz.utils.Statics.QUIZZES
import java.util.Calendar
import java.util.Date

@Entity(tableName = "quizzes")
data class Quiz(
    @PrimaryKey @ColumnInfo(name = "quiz_id") val id: String = FirebaseFirestore.getInstance()
        .collection(QUIZZES)
        .document().id,
    @ColumnInfo(name = "quiz_duration") val duration: Long,
    @ColumnInfo(name = "quiz_creator") val creatorUid: String,
    @ColumnInfo(name = "quiz_image") val image: ByteArray,
    @ColumnInfo(name = "quiz_academic_year") val academicYear: String,
    @ColumnInfo(name = "quiz_academic_subject") val subject: String,
    @ColumnInfo(name = "quiz_created_on") val creationTime: Date = Calendar.getInstance().time,
    @ColumnInfo(name = "quiz_description") val description: String,
    @ColumnInfo(name = "quiz_title") val title: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Quiz

        return image.contentEquals(other.image)
    }

    override fun hashCode(): Int {
        return image.contentHashCode()
    }
}

class DateConverter {
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }
}