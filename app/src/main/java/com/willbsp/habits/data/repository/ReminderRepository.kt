package com.willbsp.habits.data.repository

import com.willbsp.habits.data.model.Reminder
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {

    fun getAllRemindersStream(): Flow<List<Reminder>>

    fun getRemindersForHabitStream(habitId: Int): Flow<List<Reminder>>

    fun getRemindersForDayStream(day: Int): Flow<List<Reminder>>

    fun upsertReminder(reminder: Reminder)

    fun deleteReminder(reminder: Reminder)

}