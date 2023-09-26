package com.willbsp.habits.data.repository.local

import com.willbsp.habits.data.database.dao.ReminderDao
import com.willbsp.habits.data.model.Reminder
import com.willbsp.habits.data.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow
import java.time.DayOfWeek
import javax.inject.Inject

class LocalReminderRepository @Inject constructor(
    private val reminderDao: ReminderDao
) : ReminderRepository {
    override fun getAllRemindersStream(): Flow<List<Reminder>> = reminderDao.getAllRemindersStream()

    override fun getRemindersForHabitStream(habitId: Int): Flow<List<Reminder>> =
        reminderDao.getRemindersForHabitStream(habitId)

    override fun getRemindersForDayStream(day: DayOfWeek): Flow<List<Reminder>> =
        reminderDao.getRemindersForDayStream(day)

    override fun getReminderStream(reminderId: Int): Flow<Reminder?> =
        reminderDao.getReminderStream(reminderId)

    override suspend fun clearReminders(habitId: Int) = reminderDao.clearReminders(habitId)

    override suspend fun insertReminder(reminder: Reminder) = reminderDao.insert(reminder)

    override suspend fun deleteReminder(reminder: Reminder) = reminderDao.delete(reminder)


}