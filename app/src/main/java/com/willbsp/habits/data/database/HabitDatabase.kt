package com.willbsp.habits.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.willbsp.habits.data.model.Entry
import com.willbsp.habits.data.model.Habit

@Database(entities = [Habit::class, Entry::class], version = 1, exportSchema = false)
abstract class HabitDatabase : RoomDatabase() {

    abstract fun habitDao(): HabitDao
    abstract fun entryDao(): EntryDao

    abstract fun habitEntryDao(): HabitEntryDao

}