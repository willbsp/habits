package com.willbsp.habits.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.willbsp.habits.data.Habit

@Database(entities = [Habit::class], version = 1, exportSchema = false)
abstract class HabitDatabase : RoomDatabase() {

    abstract fun habitDao(): HabitDao

    companion object { // Database is a singleton

        @Volatile
        private var Instance: HabitDatabase? = null

        fun getDatabase(context: Context): HabitDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, HabitDatabase::class.java, "habit_database")
                    .fallbackToDestructiveMigration() // TODO will need to change at some point
                    .build()
                    .also { Instance = it }
            }
        }

    }

}