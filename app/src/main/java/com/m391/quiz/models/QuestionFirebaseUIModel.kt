package com.m391.quiz.models

import java.io.Serializable

data class QuestionFirebaseUIModel(
    val questionId: String,
    val quizId: String,
    val questionHeaderText: String,
    val questionBodyText: String?,
    val questionBodyImageUrl: String?,
    val questionBodyImagePath: String?,
    val answerType: String,
    val answerBodyText: Boolean,
    val answerBodyImage: Boolean,
    val answerFirstChoiceText: String?,
    val answerFirstChoiceImageUrl: String?,
    val answerFirstChoiceImagePath: String?,
    val answerSecondChoiceText: String?,
    val answerSecondChoiceImageUrl: String?,
    val answerSecondChoiceImagePath: String?,
    val answerThirdChoiceText: String?,
    val answerThirdChoiceImageUrl: String?,
    val answerThirdChoiceImagePath: String?,
    val answerFourthChoiceText: String?,
    val answerFourthChoiceImageUrl: String?,
    val answerFourthChoiceImagePath: String?,
    val questionScore: Int,
    val questionNumber: Int
) : Serializable
