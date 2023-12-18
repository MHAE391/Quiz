package com.m391.quiz.database.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.m391.quiz.models.QuestionFirebaseModel
import com.m391.quiz.models.QuizFirebaseModel
import com.m391.quiz.models.QuizModel
import com.m391.quiz.utils.Statics.QUESTIONS
import com.m391.quiz.utils.Statics.QUIZ
import com.m391.quiz.utils.Statics.QUIZZES
import com.m391.quiz.utils.Statics.QUIZ_ACADEMIC_YEAR
import com.m391.quiz.utils.Statics.QUIZ_CREATION_TIME
import com.m391.quiz.utils.Statics.QUIZ_CREATOR
import com.m391.quiz.utils.Statics.QUIZ_DESCRIPTION
import com.m391.quiz.utils.Statics.QUIZ_DURATION
import com.m391.quiz.utils.Statics.QUIZ_ID
import com.m391.quiz.utils.Statics.QUIZ_IMAGE_PATH
import com.m391.quiz.utils.Statics.QUIZ_IMAGE_URL
import com.m391.quiz.utils.Statics.QUIZ_SCORE
import com.m391.quiz.utils.Statics.QUIZ_SUBJECT
import com.m391.quiz.utils.Statics.QUIZ_TITLE
import com.m391.quiz.utils.Statics.Question
import com.m391.quiz.utils.Statics.RESPONSE_SUCCESS
import com.m391.quiz.utils.m391FirebaseModel
import com.m391.quiz.utils.m391List
import com.m391.quiz.utils.m391Score
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date

class Quizzes(private val media: MediaStorage) {
    private val firestore = FirebaseFirestore.getInstance().collection(QUIZZES)
    private var registration: ListenerRegistration? = null

    suspend fun uploadQuiz(quiz: QuizModel): String = withContext(Dispatchers.IO) {
        var result = RESPONSE_SUCCESS
        val bodyImage = media.uploadImageByteArray(quiz.image, QUIZ)
        val firebaseQuestions = ArrayList<QuestionFirebaseModel>()
        quiz.questions.forEach { question ->
            val questionBodyImage = media.uploadImageByteArray(question.questionBodyImage, Question)
            val firstAnswerImage =
                media.uploadImageByteArray(question.answerFirstChoiceImage, Question)
            val secondAnswerImage =
                media.uploadImageByteArray(question.answerSecondChoiceImage, Question)
            val thirdAnswerImage =
                media.uploadImageByteArray(question.answerThirdChoiceImage, Question)
            val fourthAnswerImage =
                media.uploadImageByteArray(question.answerFourthChoiceImage, Question)
            val firebaseQuestion = question.m391FirebaseModel(
                bodyImagePath = questionBodyImage.first,
                bodyImageUrl = questionBodyImage.second,
                firstChoiceImagePath = firstAnswerImage.first,
                firstChoiceImageUrl = firstAnswerImage.second,
                secondChoiceImagePath = secondAnswerImage.first,
                secondChoiceImageUrl = secondAnswerImage.second,
                thirdChoiceImagePath = thirdAnswerImage.first,
                thirdChoiceImageUrl = thirdAnswerImage.second,
                fourthChoiceImagePath = fourthAnswerImage.first,
                fourthChoiceImageUrl = fourthAnswerImage.second
            )
            firebaseQuestions.add(firebaseQuestion)
        }
        try {
            val firebaseQuiz =
                hashMapOf(
                    QUESTIONS to firebaseQuestions,
                    QUIZ_ID to quiz.id,
                    QUIZ_CREATOR to quiz.creatorUid,
                    QUIZ_DESCRIPTION to quiz.description,
                    QUIZ_ACADEMIC_YEAR to quiz.academicYear,
                    QUIZ_SUBJECT to quiz.subject,
                    QUIZ_TITLE to quiz.title,
                    QUIZ_DURATION to quiz.duration,
                    QUIZ_CREATION_TIME to Calendar.getInstance().time,
                    QUIZ_IMAGE_PATH to bodyImage.first,
                    QUIZ_IMAGE_URL to bodyImage.second,
                    QUIZ_SCORE to quiz.questions.m391Score()
                )
            firestore.document(quiz.id)
                .set(firebaseQuiz).addOnFailureListener {
                    result = it.localizedMessage!!.toString()
                }.await()
        } catch (e: Exception) {
            result = e.localizedMessage!!.toString()
        }
        return@withContext result
    }

    suspend fun getAllQuizzes(): LiveData<List<QuizFirebaseModel>> =
        withContext(Dispatchers.IO) {
            val quizzes = MutableLiveData<List<QuizFirebaseModel>>()
            registration = firestore.addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Quizzes Listener", "Listen failed.")
                    return@addSnapshotListener
                }
                val quizzesArray = ArrayList<QuizFirebaseModel>()
                try {
                    value?.forEach { quiz ->

                        val firebaseQuiz = QuizFirebaseModel(
                            quiz_id = quiz.get(QUIZ_ID, String::class.java)!!,
                            quiz_duration = quiz.get(QUIZ_DURATION, Long::class.java)!!,
                            quiz_description = quiz.get(QUIZ_DESCRIPTION, String::class.java)!!,
                            quiz_creation_time = quiz.get(QUIZ_CREATION_TIME, Date::class.java)!!,
                            quiz_creator = quiz.get(QUIZ_CREATOR, String::class.java)!!,
                            quiz_subject = quiz.get(QUIZ_SUBJECT, String::class.java)!!,
                            quiz_score = quiz.get(QUIZ_SCORE, Int::class.java)!!,
                            quiz_academic_year = quiz.get(QUIZ_ACADEMIC_YEAR, String::class.java)!!,
                            quiz_title = quiz.get(QUIZ_TITLE, String::class.java)!!,
                            quiz_image_path = quiz.get(QUIZ_IMAGE_PATH, String::class.java)!!,
                            quiz_image_url = quiz.get(QUIZ_IMAGE_URL, String::class.java)!!,
                            questions = (quiz.get(QUESTIONS) as List<HashMap<String, Any>>).m391List()
                                .shuffled()
                        )
                        quizzesArray.add(firebaseQuiz)
                        quizzes.postValue(quizzesArray)
                    }
                } catch (e: Exception) {
                    Log.e("QuizListener", e.localizedMessage!!)
                }
            }

            return@withContext quizzes
        }

    suspend fun closeQuizzesStream() = withContext(Dispatchers.IO) {
        if (registration != null) {
            registration!!.remove()
        }
    }
}