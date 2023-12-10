package com.m391.quiz.database.local.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.m391.quiz.database.local.entities.DateConverter
import com.m391.quiz.database.local.entities.Quiz
import com.m391.quiz.database.local.room.dao.QuizDAO

@Database(entities = [Quiz::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun quizDao(): QuizDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "quiz_trrrsssy_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}