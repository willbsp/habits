package com.willbsp.habits.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.willbsp.habits.data.database.dao.EntryDao
import com.willbsp.habits.data.database.dao.HabitDao
import com.willbsp.habits.data.database.dao.HabitWithEntriesDao
import com.willbsp.habits.data.database.util.Converters
import com.willbsp.habits.data.model.Entry
import com.willbsp.habits.data.model.Habit

@Database(entities = [Habit::class, Entry::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class HabitDatabase : RoomDatabase() {

    abstract fun habitDao(): HabitDao
    abstract fun entryDao(): EntryDao
    abstract fun habitWithEntriesDao(): HabitWithEntriesDao

}