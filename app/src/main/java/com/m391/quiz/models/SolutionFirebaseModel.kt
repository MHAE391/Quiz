package com.m391.quiz.models

data class SolutionFirebaseModel(
    val question: QuestionFirebaseModel?,
    val textAnswer: String?,
    val imageAnswer: String?,
    val mcqAnswer: String?,
)