package com.m391.quiz.ui.teacher

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.m391.quiz.database.local.entities.Quiz
import com.m391.quiz.database.local.interfaces.QuizInterface
import com.m391.quiz.ui.shared.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TeacherViewModel(private val app: Application, private val quizRepository: QuizInterface) :
    BaseViewModel(app) {
    val insertQuiz: suspend (Quiz) -> Unit = { quiz ->
        withContext(Dispatchers.IO) {
            quizRepository.insertQuiz(quiz)
        }
    }


    val deleteQuiz: suspend (Int) -> Unit = { quizId ->
        withContext(Dispatchers.IO) {
            val quiz = getQuizById(quizId)
            quiz?.apply {
                quizRepository.deleteQuiz(this)
            }
        }
    }

    val getAllQuizzes: suspend () -> List<Quiz> =
        {
            withContext(Dispatchers.IO) {
                return@withContext quizRepository.getAllQuizzes()
            }
        }

    val getQuizById: suspend (quizId: Int) -> Quiz? =
        { quizId ->
            withContext(Dispatchers.IO) {
                return@withContext quizRepository.getQuizById(quizId)
            }
        }

    val deleteAllQuizzes: suspend () -> Unit =
        {
            withContext(Dispatchers.IO) {
                quizRepository.deleteAllQuizzes()
            }
        }

}