package com.willbsp.habits.fake.dao

import com.willbsp.habits.data.database.dao.ReminderDao
import com.willbsp.habits.data.model.Reminder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.time.DayOfWeek

class FakeReminderDao : ReminderDao {

    val reminders = mutableListOf<Reminder>()
    private var observableReminders = MutableStateFlow<List<Reminder>>(listOf())
    private suspend fun emit() = observableReminders.emit(reminders.toList())

    override fun getAllRemindersStream(): Flow<List<Reminder>> = observableReminders

    override fun getRemindersForHabitStream(habitId: Int): Flow<List<Reminder>> =
        observableReminders.map { reminders -> reminders.filter { it.habitId == habitId } }

    override fun getRemindersForDayStream(day: DayOfWeek): Flow<List<Reminder>> =
        observableReminders.map { reminders -> reminders.filter { it.day == day } }

    override fun getReminderStream(reminderId: Int): Flow<Reminder?> =
        observableReminders.map { reminders -> reminders.find { it.id == reminderId } }

    override suspend fun clearReminders(habitId: Int) {
        reminders.removeAll { it.habitId == habitId }
        emit()
    }

    override suspend fun insert(reminder: Reminder) {
        reminders.add(reminder)
        emit()
    }

    override suspend fun delete(reminder: Reminder) {
        reminders.removeAll { it == reminder }
        emit()
    }
}