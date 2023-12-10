package com.m391.quiz.database.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.m391.quiz.database.local.entities.Quiz
import com.m391.quiz.database.local.interfaces.QuizInterface

@Dao
interface QuizDAO : QuizInterface {

    @Insert
    override suspend fun insertQuiz(quiz: Quiz)

    @Delete
    override suspend fun deleteQuiz(quiz: Quiz)

    @Query("SELECT * FROM quizzes")
    override suspend fun getAllQuizzes(): List<Quiz>

    @Query("SELECT * FROM quizzes WHERE quiz_id = :quizId")
    override suspend fun getQuizById(quizId: Int): Quiz?

    @Query("DELETE FROM quizzes")
    override suspend fun deleteAllQuizzes()
}