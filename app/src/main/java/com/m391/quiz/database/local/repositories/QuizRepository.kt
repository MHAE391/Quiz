package com.m391.quiz.database.local.repositories

import android.content.Context
import com.m391.quiz.database.local.entities.Question
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

    override suspend fun getAllQuizQuestions(quizId: Int): List<Question> =
        withContext(dispatcher) {
            return@withContext quizDAO.getAllQuizQuestions(quizId)
        }

    override suspend fun insertQuestion(question: Question) =
        withContext(dispatcher) {
            quizDAO.insertQuestion(question)
        }

    override suspend fun deleteQuestion(question: Question) =
        withContext(dispatcher) {
            quizDAO.deleteQuestion(question)
        }

    override suspend fun deleteAllQuestions() =
        withContext(dispatcher) {
            quizDAO.deleteAllQuestions()
        }

    override suspend fun getQuestionById(questionId: Int): Question? =
        withContext(dispatcher) {
            return@withContext quizDAO.getQuestionById(questionId)
        }


}