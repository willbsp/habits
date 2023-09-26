package com.willbsp.habits.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.willbsp.habits.data.model.Reminder
import kotlinx.coroutines.flow.Flow
import java.time.DayOfWeek

@Dao
interface ReminderDao {

    @Query("SELECT * FROM reminders")
    fun getAllRemindersStream(): Flow<List<Reminder>>

    @Query("SELECT * FROM reminders WHERE habit_id = :habitId")
    fun getRemindersForHabitStream(habitId: Int): Flow<List<Reminder>>

    @Query("SELECT * FROM reminders WHERE day = :day")
    fun getRemindersForDayStream(day: DayOfWeek): Flow<List<Reminder>>

    @Query("SELECT * FROM reminders WHERE id = :reminderId")
    fun getReminderStream(reminderId: Int): Flow<Reminder?>

    @Query("DELETE FROM reminders WHERE habit_id = :habitId")
    suspend fun clearReminders(habitId: Int)

    @Insert
    suspend fun insert(reminder: Reminder)

    @Delete
    suspend fun delete(reminder: Reminder)

}