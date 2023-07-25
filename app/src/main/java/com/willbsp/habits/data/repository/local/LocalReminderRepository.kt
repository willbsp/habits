package com.willbsp.habits.data.repository.local

import com.willbsp.habits.data.database.dao.ReminderDao
import com.willbsp.habits.data.model.Reminder
import com.willbsp.habits.data.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalReminderRepository @Inject constructor(
    private val reminderDao: ReminderDao
) : ReminderRepository {
    override fun getAllRemindersStream(): Flow<List<Reminder>> = reminderDao.getAllRemindersStream()

    override fun getRemindersForHabitStream(habitId: Int): Flow<List<Reminder>> =
        reminderDao.getRemindersForHabitStream(habitId)

    override fun getRemindersForDayStream(day: Int): Flow<List<Reminder>> =
        reminderDao.getRemindersForDayStream(day)

    override suspend fun upsertReminder(reminder: Reminder) = reminderDao.upsert(reminder)

    override suspend fun deleteReminder(reminder: Reminder) = reminderDao.delete(reminder)


}