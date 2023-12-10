package com.m391.quiz.database.local.interfaces

import com.m391.quiz.database.local.entities.Quiz

interface QuizInterface {
    suspend fun insertQuiz(quiz: Quiz)
    suspend fun deleteQuiz(quiz: Quiz)
    suspend fun getAllQuizzes(): List<Quiz>
    suspend fun getQuizById(quizId: Int): Quiz?
    suspend fun deleteAllQuizzes()
}