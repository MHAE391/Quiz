package com.m391.quiz.models

data class StudentFirebaseModel(
    val uid: String,
    val firstName: String,
    val lastName: String,
    val imageUrl: String,
    val imagePath: String,
    val academicYear: String,
    val subjects: List<String>,
    val dateOfBarth: String
)