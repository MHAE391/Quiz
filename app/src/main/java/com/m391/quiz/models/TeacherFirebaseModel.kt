package com.m391.quiz.models

data class TeacherFirebaseModel(
    val uid: String,
    val firstName: String,
    val lastName: String,
    val imageUrl: String,
    val imagePath: String,
    val academicYears: List<String>,
    val subjects: List<String>,
    val dateOfBarth: String
)