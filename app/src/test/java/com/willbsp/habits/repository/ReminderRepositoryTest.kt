package com.willbsp.habits.repository

import com.willbsp.habits.data.TestData.reminder1
import com.willbsp.habits.data.TestData.reminder2
import com.willbsp.habits.data.TestData.reminder3
import com.willbsp.habits.data.model.Reminder
import com.willbsp.habits.data.repository.ReminderRepository
import com.willbsp.habits.data.repository.local.LocalReminderRepository
import com.willbsp.habits.fake.dao.FakeReminderDao
import com.willbsp.habits.fake.repository.FakeReminderRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import kotlin.reflect.KClass

@RunWith(Parameterized::class)
class ReminderRepositoryTest(
    private val repositoryClass: KClass<ReminderRepository>
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun classes(): List<KClass<out ReminderRepository>> {
            return listOf(
                LocalReminderRepository::class,
                FakeReminderRepository::class
            )
        }
    }

    private lateinit var repository: ReminderRepository

    @Before
    fun setup() {
        when (repositoryClass) {
            LocalReminderRepository::class ->
                repository = LocalReminderRepository(FakeReminderDao())

            FakeReminderRepository::class ->
                repository = FakeReminderRepository()
        }
    }

    @Test
    fun getAllRemindersStream_whenRemindersAdded_returnsReminders() = runTest {
        val reminderStream = repository.getAllRemindersStream()
        repository.insertReminder(reminder1)
        assertEquals(listOf(reminder1), reminderStream.first())
        repository.insertReminder(reminder2)
        assertEquals(listOf(reminder1, reminder2), reminderStream.first())
    }

    @Test
    fun getAllRemindersStream_whenRemindersDeleted_returnsEmptyList() = runTest {
        val reminderStream = repository.getAllRemindersStream()
        repository.insertReminder(reminder1)
        repository.insertReminder(reminder2)
        assertEquals(listOf(reminder1, reminder2), reminderStream.first())
        repository.deleteReminder(reminder1)
        repository.deleteReminder(reminder2)
        assertEquals(listOf<Reminder>(), reminderStream.first())
    }

    @Test
    fun getRemindersForHabitStream_whenRemindersAdded_returnsReminders() = runTest {
        val reminderStream = repository.getRemindersForHabitStream(reminder1.habitId)
        assertEquals(listOf<Reminder>(), reminderStream.first())
        repository.insertReminder(reminder1)
        repository.insertReminder(reminder2)
        repository.insertReminder(reminder3)
        assertEquals(listOf(reminder1, reminder2), reminderStream.first())
    }

    @Test
    fun getRemindersForHabitStream_whenRemindersDeleted_returnsEmptyList() = runTest {
        val reminderStream = repository.getRemindersForHabitStream(reminder1.habitId)
        repository.insertReminder(reminder1)
        repository.insertReminder(reminder2)
        repository.insertReminder(reminder3)
        assertEquals(listOf(reminder1, reminder2), reminderStream.first())
        repository.deleteReminder(reminder1)
        repository.deleteReminder(reminder2)
        assertEquals(listOf<Reminder>(), reminderStream.first())
    }

    @Test
    fun getRemindersForDayStream_whenRemindersAdded_returnsReminders() = runTest {
        val reminderStream = repository.getRemindersForDayStream(reminder1.day)
        assertEquals(listOf<Reminder>(), reminderStream.first())
        repository.insertReminder(reminder1)
        repository.insertReminder(reminder2)
        repository.insertReminder(reminder3)
        assertEquals(listOf(reminder1, reminder2), reminderStream.first())
    }

    @Test
    fun getRemindersForDayStream_whenRemindersDeleted_returnsEmptyList() = runTest {
        val reminderStream = repository.getRemindersForDayStream(reminder1.day)
        repository.insertReminder(reminder1)
        repository.insertReminder(reminder2)
        repository.insertReminder(reminder3)
        assertEquals(listOf(reminder1, reminder2), reminderStream.first())
        repository.deleteReminder(reminder1)
        repository.deleteReminder(reminder2)
        assertEquals(listOf<Reminder>(), reminderStream.first())
    }

    @Test
    fun getReminderStream_whenReminderExists_returnReminder() = runTest {
        repository.insertReminder(reminder1)
        assertEquals(reminder1, repository.getReminderStream(reminder1.id).first())
    }

    @Test
    fun getReminderStream_whenReminderDoesNotExists_returnNull() = runTest {
        assertEquals(null, repository.getReminderStream(reminder1.id).first())
    }

    @Test
    fun clearReminders_whenRemindersExist_clearsRemindersForHabit() = runTest {
        repository.insertReminder(reminder1)
        repository.insertReminder(reminder2)
        repository.insertReminder(reminder3)
        assertEquals(3, repository.getAllRemindersStream().first().count())
        repository.clearReminders(reminder1.habitId)
        assertEquals(listOf(reminder3), repository.getAllRemindersStream().first())
    }

    @Test
    fun insertReminder_validReminder_addsReminder() = runTest {
        repository.insertReminder(reminder1)
        assertEquals(reminder1, repository.getAllRemindersStream().first().first())
    }

    @Test
    fun deleteReminder_validReminder_deletesReminder() = runTest {
        repository.insertReminder(reminder1)
        assertEquals(reminder1, repository.getAllRemindersStream().first().first())
        repository.deleteReminder(reminder1)
        assertEquals(listOf<Reminder>(), repository.getAllRemindersStream().first())
    }

}