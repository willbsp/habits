package com.willbsp.habits.data.repository

import com.willbsp.habits.data.model.Reminder
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {

    fun getAllRemindersStream(): Flow<List<Reminder>>

    fun getRemindersForHabitStream(habitId: Int): Flow<List<Reminder>>

    fun getRemindersForDayStream(day: Int): Flow<List<Reminder>>

    suspend fun upsertReminder(reminder: Reminder)

    suspend fun deleteReminder(reminder: Reminder)

}