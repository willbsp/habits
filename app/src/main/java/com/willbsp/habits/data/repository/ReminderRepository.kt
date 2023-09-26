package com.willbsp.habits.data.repository

import com.willbsp.habits.data.model.Reminder
import kotlinx.coroutines.flow.Flow
import java.time.DayOfWeek

interface ReminderRepository {

    fun getAllRemindersStream(): Flow<List<Reminder>>

    fun getRemindersForHabitStream(habitId: Int): Flow<List<Reminder>>

    fun getRemindersForDayStream(day: DayOfWeek): Flow<List<Reminder>>

    fun getReminderStream(reminderId: Int): Flow<Reminder?>

    suspend fun insertReminder(reminder: Reminder)

    suspend fun deleteReminder(reminder: Reminder)

    suspend fun clearReminders(habitId: Int)

}