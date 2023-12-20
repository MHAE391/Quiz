package com.m391.quiz.database.remote

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.util.splitToIntList
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.m391.quiz.models.QuestionFirebaseUIModel
import com.m391.quiz.models.QuestionScore
import com.m391.quiz.models.SolutionFirebaseModel
import com.m391.quiz.models.StudentFirebaseModel
import com.m391.quiz.utils.Statics.ANSWER_COMMENT
import com.m391.quiz.utils.Statics.QUESTION_ID
import com.m391.quiz.utils.Statics.QUESTION_LOWER_CASE
import com.m391.quiz.utils.Statics.QUIZZES_SCORES
import com.m391.quiz.utils.Statics.QUIZZES_SOLVERS
import com.m391.quiz.utils.Statics.QUIZ_SOLVER
import com.m391.quiz.utils.Statics.RESPONSE_SUCCESS
import com.m391.quiz.utils.Statics.SOLUTIONS
import com.m391.quiz.utils.Statics.SOLUTION_IMAGE_PATH
import com.m391.quiz.utils.Statics.SOLUTION_IMAGE_URL
import com.m391.quiz.utils.Statics.SOLUTION_MCQ
import com.m391.quiz.utils.Statics.SOLUTION_TEXT
import com.m391.quiz.utils.Statics.SOLVER_ERROR_RESPONSE
import com.m391.quiz.utils.Statics.SOLVER_SUCCESS_RESPONSE
import com.m391.quiz.utils.Statics.START_TIME
import com.m391.quiz.utils.Statics.STUDENT_SCORE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.w3c.dom.Comment
import java.util.Calendar

class Solutions(
    private val auth: Authentication,
    private val mediaStorage: MediaStorage
) {
    private val firestore = FirebaseFirestore.getInstance()
    private var registration: ListenerRegistration? = null

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
            firestore.collection(SOLUTIONS).document(questionAnswer.question!!.quiz_id)
                .collection(currentUserUid).document(questionAnswer.question.question_id)
                .set(solution).addOnFailureListener {
                    response = it.localizedMessage!!
                }.await()
            return@withContext response
        }

    suspend fun uploadQuestionScore(
        studentUid: String,
        questionUid: String,
        quizUid: String,
        score: Int,
        comment: String?
    ): String = withContext(Dispatchers.IO) {
        var result = RESPONSE_SUCCESS
        val scores = mapOf(
            QUESTION_ID to questionUid,
            STUDENT_SCORE to score,
            ANSWER_COMMENT to comment
        )
        firestore.collection(QUIZZES_SCORES).document(quizUid)
            .collection(studentUid).document(questionUid).set(
                scores
            ).addOnFailureListener {
                result = it.localizedMessage!!
            }.await()
        return@withContext result
    }

    suspend fun getStudentQuizScores(
        studentUid: String,
        quizUid: String
    ): LiveData<List<QuestionScore>> = withContext(Dispatchers.IO) {
        val studentScores = MutableLiveData<List<QuestionScore>>()
        registration = firestore.collection(QUIZZES_SCORES).document(quizUid)
            .collection(studentUid).addSnapshotListener { value, error ->
                if (error != null)
                    Log.e("Score Listener", error.localizedMessage!!)

                if (value != null) {
                    val scoresList = ArrayList<QuestionScore>()
                    value.forEach { score ->
                        scoresList.add(
                            QuestionScore(
                                score.get(QUESTION_ID, String::class.java)!!,
                                score.get(STUDENT_SCORE, Int::class.java)!!,
                                score.get(ANSWER_COMMENT, String::class.java)
                            )
                        )
                        studentScores.postValue(scoresList)
                    }
                }
            }
        return@withContext studentScores
    }

    suspend fun getStudentQuestionScore(
        studentUid: String,
        quizUid: String,
        questionId: String
    ): LiveData<QuestionScore> = withContext(Dispatchers.IO) {
        val studentScores = MutableLiveData<QuestionScore>()
        registration = firestore.collection(QUIZZES_SCORES).document(quizUid)
            .collection(studentUid).document(questionId).addSnapshotListener { value, error ->
                if (error != null)
                    Log.e("Score Listener", error.localizedMessage!!)
                value?.apply {
                    val score =
                        QuestionScore(
                            this.get(QUESTION_ID, String::class.java),
                            this.get(STUDENT_SCORE, Int::class.java),
                            this.get(ANSWER_COMMENT, String::class.java)
                        )
                    studentScores.postValue(score)

                }
            }
        return@withContext studentScores
    }

    suspend fun getStudentSolution(
        studentUid: String,
        questionUid: String,
        quizUid: String
    ): LiveData<SolutionFirebaseModel> =
        withContext(Dispatchers.IO) {
            val solution = MutableLiveData<SolutionFirebaseModel>()
            registration = firestore.collection(SOLUTIONS).document(quizUid)
                .collection(studentUid).document(questionUid).addSnapshotListener { value, error ->
                    if (error != null) {
                        Log.e("Solvers Listener", error.localizedMessage!!)
                        return@addSnapshotListener
                    } else {
                        if (value != null) {
                            val sol = SolutionFirebaseModel(
                                question = null,
                                textAnswer = value.get(SOLUTION_TEXT, String::class.java),
                                imageAnswer = value.get(SOLUTION_IMAGE_URL, String::class.java),
                                mcqAnswer = value.get(SOLUTION_MCQ, String::class.java)
                            )
                            solution.postValue(sol)
                        }
                    }
                }
            return@withContext solution
        }

    suspend fun startQuiz(quizId: String): String = withContext(Dispatchers.IO) {
        var response = SOLVER_SUCCESS_RESPONSE
        val currentUserUid = auth.getCurrentUser()!!.uid
        val solver = firestore.collection(QUIZZES_SOLVERS).document(quizId).collection(QUIZ_SOLVER)
            .document(currentUserUid).get().await()
        if (solver.exists()) response = SOLVER_ERROR_RESPONSE
        else {
            val map = mapOf(
                START_TIME to Calendar.getInstance().time,
                QUIZ_SOLVER to currentUserUid
            )
            firestore.collection(QUIZZES_SOLVERS).document(quizId).collection(QUIZ_SOLVER)
                .document(currentUserUid).set(map).await()
        }
        return@withContext response
    }

    suspend fun getAllQuizStudentsSolvers(
        quizId: String
    ): LiveData<List<String>> =
        withContext(Dispatchers.IO) {
            val solvers = MutableLiveData<List<String>>()
            registration =
                firestore.collection(QUIZZES_SOLVERS).document(quizId).collection(QUIZ_SOLVER)
                    .addSnapshotListener { value, error ->
                        if (error != null) {
                            Log.e("Solvers Listener", error.localizedMessage!!)
                            return@addSnapshotListener
                        } else {
                            if (value != null) {
                                val solversList = ArrayList<String>()
                                value.forEach { solverQuery ->
                                    solversList.add(solverQuery[QUIZ_SOLVER].toString())
                                    solvers.postValue(solversList)
                                }
                            }

                        }
                    }
            return@withContext solvers
        }

    suspend fun closeSolutionsStream() = withContext(Dispatchers.IO) {
        if (registration != null) {
            registration!!.remove()
        }
    }
}