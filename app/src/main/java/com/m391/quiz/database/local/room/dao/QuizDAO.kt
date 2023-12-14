package com.m391.quiz.database.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.m391.quiz.database.local.entities.Question
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

    @Query("SELECT * FROM questions WHERE quiz_id = :quizId")
    override suspend fun getAllQuizQuestions(quizId: Int): List<Question>

    @Query("SELECT * FROM questions WHERE quiz_id = :questionId")
    override suspend fun getQuestionById(questionId: Int): Question?

    @Insert
    override suspend fun insertQuestion(question: Question)

    @Delete
    override suspend fun deleteQuestion(question: Question)

    @Query("DELETE FROM questions")
    override suspend fun deleteAllQuestions()
}