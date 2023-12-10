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
    @ColumnInfo(name = "question_id") val id: Int,
    @ColumnInfo(name = "quiz_id") val quizId: Int,
    @ColumnInfo(name = "question_type") val type: String,
    @ColumnInfo(name = "question_number") val number: Int
)
