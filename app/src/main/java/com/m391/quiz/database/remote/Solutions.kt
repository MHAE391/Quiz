package com.m391.quiz.database.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.m391.quiz.models.SolutionFirebaseModel
import com.m391.quiz.utils.Statics.QUESTION_LOWER_CASE
import com.m391.quiz.utils.Statics.QUIZZES_SOLVERS
import com.m391.quiz.utils.Statics.RESPONSE_SUCCESS
import com.m391.quiz.utils.Statics.SOLUTIONS
import com.m391.quiz.utils.Statics.SOLUTION_IMAGE_PATH
import com.m391.quiz.utils.Statics.SOLUTION_IMAGE_URL
import com.m391.quiz.utils.Statics.SOLUTION_MCQ
import com.m391.quiz.utils.Statics.SOLUTION_TEXT
import com.m391.quiz.utils.Statics.SOLVER_ERROR_RESPONSE
import com.m391.quiz.utils.Statics.SOLVER_SUCCESS_RESPONSE
import com.m391.quiz.utils.Statics.START_TIME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Calendar

class Solutions(private val auth: Authentication, private val mediaStorage: MediaStorage) {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun uploadQuestionAnswer(questionAnswer: SolutionFirebaseModel): String =
        withContext(Dispatchers.IO) {
            var response = RESPONSE_SUCCESS
            val currentUserUid = auth.getCurrentUser()!!.uid
            val image = mediaStorage.uploadImageSolution(
                questionAnswer.imageAnswer
            )
            val solution = mapOf(
                QUESTION_LOWER_CASE to questionAnswer.question,
                SOLUTION_TEXT to questionAnswer.textAnswer,
                SOLUTION_IMAGE_URL to image.second,
                SOLUTION_IMAGE_PATH to image.first,
                SOLUTION_MCQ to questionAnswer.mcqAnswer
            )
            firestore.collection(SOLUTIONS).document(questionAnswer.question.quiz_id)
                .collection(currentUserUid).document(questionAnswer.question.question_id)
                .set(solution).addOnFailureListener {
                    response = it.localizedMessage!!
                }.await()
            return@withContext response
        }

    suspend fun startQuiz(quizId: String): String = withContext(Dispatchers.IO) {
        var response = SOLVER_SUCCESS_RESPONSE
        val currentUserUid = auth.getCurrentUser()!!.uid
        val solver = firestore.collection(QUIZZES_SOLVERS).document(currentUserUid).get().await()
        if (solver.exists()) response = SOLVER_ERROR_RESPONSE
        else {
            val map = mapOf(
                START_TIME to Calendar.getInstance().time
            )
            firestore.collection(QUIZZES_SOLVERS).document(currentUserUid).set(map).await()
        }
        return@withContext response
    }
}