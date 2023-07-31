package com.willbsp.habits.fake.repository

import com.willbsp.habits.data.model.Reminder
import com.willbsp.habits.data.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeReminderRepository : ReminderRepository {

    val reminders = mutableListOf<Reminder>()
    private var observableReminders = MutableStateFlow<List<Reminder>>(listOf())
    private suspend fun emit() = observableReminders.emit(reminders.toList())

    override fun getAllRemindersStream(): Flow<List<Reminder>> = observableReminders

    override fun getRemindersForHabitStream(habitId: Int): Flow<List<Reminder>> =
        observableReminders.map { reminders -> reminders.filter { it.habitId == habitId } }

    override fun getRemindersForDayStream(day: Int): Flow<List<Reminder>> =
        observableReminders.map { reminders -> reminders.filter { it.day == day } }

    override suspend fun insertReminder(reminder: Reminder) {
        reminders.add(reminder)
        emit()
    }

    override suspend fun deleteReminder(reminder: Reminder) {
        reminders.remove(reminder)
        emit()
    }

    override suspend fun clearReminders(habitId: Int) {
        reminders.removeAll { it.habitId == habitId }
        emit()
    }

}