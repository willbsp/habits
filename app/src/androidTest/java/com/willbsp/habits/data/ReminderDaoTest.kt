package com.willbsp.habits.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.willbsp.habits.data.TestData.habit2
import com.willbsp.habits.data.TestData.reminder1
import com.willbsp.habits.data.TestData.reminder2
import com.willbsp.habits.data.database.HabitDatabase
import com.willbsp.habits.data.database.dao.HabitDao
import com.willbsp.habits.data.database.dao.ReminderDao
import com.willbsp.habits.data.model.Reminder
import com.willbsp.habits.fake.dao.FakeHabitDao
import com.willbsp.habits.fake.dao.FakeReminderDao
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.DayOfWeek

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
open class ReminderDaoTest {

    lateinit var habitDao: HabitDao
    lateinit var reminderDao: ReminderDao
    private lateinit var habitDatabase: HabitDatabase

    @Before
    open fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        habitDatabase = Room.inMemoryDatabaseBuilder(context, HabitDatabase::class.java).build()
        habitDao = habitDatabase.habitDao()
        reminderDao = habitDatabase.reminderDao()
    }

    @After
    @Throws(IOException::class)
    open fun closeDb() {
        habitDatabase.close()
    }

    @Test
    fun getAllRemindersStream_whenNoReminders_emptyList() = runTest {
        habitDao.insert(habit2)
        assertEquals(listOf<Reminder>(), reminderDao.getAllRemindersStream().first())
    }

    @Test
    fun getAllRemindersStream_whenReminders_returnReminders() = runTest {
        habitDao.insert(habit2)
        reminderDao.insert(reminder1)
        assertEquals(listOf(reminder1), reminderDao.getAllRemindersStream().first())
    }

    @Test
    fun getAllRemindersForHabitStream_whenReminders_returnReminders() = runTest {
        habitDao.insert(habit2)
        reminderDao.insert(reminder1)
        assertEquals(reminder1, reminderDao.getRemindersForHabitStream(habit2.id).first().first())
    }

    @Test
    fun getRemindersForDayStream_whenReminders_returnRemindersForDay() = runTest {
        habitDao.insert(habit2)
        reminderDao.insert(reminder1)
        assertEquals(
            listOf(reminder1),
            reminderDao.getRemindersForDayStream(DayOfWeek.MONDAY).first()
        )
    }

    @Test
    fun getReminderStream_whenExists_returnReminder() = runTest {
        habitDao.insert(habit2)
        reminderDao.insert(reminder1)
        assertEquals(reminder1, reminderDao.getReminderStream(reminder1.id).first())
    }

    @Test
    fun getReminderStream_doesNotExist_returnNull() = runTest {
        assertEquals(null, reminderDao.getReminderStream(reminder1.id).first())
    }

    @Test
    fun clearReminders_remindersExists_deletesAllReminders() = runTest {
        habitDao.insert(habit2)
        reminderDao.insert(reminder1)
        reminderDao.insert(reminder2)
        assertEquals(2, reminderDao.getAllRemindersStream().first().count())
        reminderDao.clearReminders(reminder1.habitId)
        assertEquals(0, reminderDao.getAllRemindersStream().first().count())
    }

}

class FakeReminderDaoTest : ReminderDaoTest() {

    @Before
    override fun createDb() {
        habitDao = FakeHabitDao()
        reminderDao = FakeReminderDao()
    }

    @After
    override fun closeDb() {
    }

}
