package com.m391.quiz.database.local.repositories

import android.content.Context
import com.m391.quiz.database.local.entities.Quiz
import com.m391.quiz.database.local.interfaces.QuizInterface
import com.m391.quiz.database.local.room.database.AppDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QuizRepository(
    context: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : QuizInterface {
    private val database = AppDatabase.getDatabase(context)
    private val quizDAO = database.quizDao()
    override suspend fun insertQuiz(quiz: Quiz) = withContext(dispatcher) {
        quizDAO.insertQuiz(quiz)
    }

    override suspend fun deleteQuiz(quiz: Quiz) = withContext(dispatcher) {
        quizDAO.deleteQuiz(quiz)
    }

    override suspend fun getAllQuizzes(): List<Quiz> = withContext(dispatcher) {
        return@withContext quizDAO.getAllQuizzes()
    }

    override suspend fun getQuizById(quizId: Int): Quiz? = withContext(dispatcher) {
        return@withContext quizDAO.getQuizById(quizId)
    }

    override suspend fun deleteAllQuizzes() = withContext(dispatcher) {
        quizDAO.deleteAllQuizzes()
    }


}