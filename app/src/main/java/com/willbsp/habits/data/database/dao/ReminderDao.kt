package com.willbsp.habits.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.willbsp.habits.data.model.Reminder
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Query("SELECT * FROM reminders")
    fun getAllRemindersStream(): Flow<List<Reminder>>

    @Query("SELECT * FROM reminders WHERE habit_id = :habitId")
    fun getRemindersForHabit(habitId: Int): Flow<List<Reminder>>

    @Query("SELECT * FROM reminders WHERE day = :day")
    fun getRemindersForDay(day: Int): Flow<List<Reminder>>

    @Upsert
    suspend fun upsert(reminder: Reminder)

    @Delete
    suspend fun delete(reminder: Reminder)

}