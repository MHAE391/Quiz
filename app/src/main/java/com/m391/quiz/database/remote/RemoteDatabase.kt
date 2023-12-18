package com.m391.quiz.database.remote

class RemoteDatabase {
    private val mediaStorage = MediaStorage()
    val authentication = Authentication()
    val information = Information(authentication, mediaStorage)
    val quizzes = Quizzes(mediaStorage)
    val students = Students()
    val teachers = Teachers()
}