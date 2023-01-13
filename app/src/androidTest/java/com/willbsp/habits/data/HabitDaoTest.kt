package com.willbsp.habits.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.willbsp.habits.data.database.HabitDao
import com.willbsp.habits.data.database.HabitDatabase
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.model.HabitFrequency
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class HabitDaoTest {

    private lateinit var habitDao: HabitDao
    private lateinit var habitDatabase: HabitDatabase
    private val habit1 = Habit(1, "Running", HabitFrequency.DAILY)
    private val habit2 = Habit(2, "Reading", HabitFrequency.WEEKLY)

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        habitDatabase = Room.inMemoryDatabaseBuilder(context, HabitDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        habitDao = habitDatabase.habitDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        habitDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllHabits_returnsAllHabitsFromDB() = runBlocking {
        addTwoHabitsToDb()
        val habits = habitDao.getAllHabits().first()
        assertEquals(habits[0], habit1)
        assertEquals(habits[1], habit2)
    }

    @Test
    @Throws(Exception::class)
    fun daoInsertHabit_insertsHabitIntoDb() = runBlocking {
        addOneHabitToDb()
        val habits = habitDao.getAllHabits().first()
        assertEquals(habits[0], habit1)
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteHabit_deletesHabitsFromDb() = runBlocking {
        addTwoHabitsToDb()
        habitDao.delete(habit1)
        habitDao.delete(habit2)
        val habits = habitDao.getAllHabits().first()
        assertTrue(habits.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdateHabit_updatesHabitsInDb() = runBlocking {
        addTwoHabitsToDb()
        val updateHabit1 = habit1.copy(frequency = HabitFrequency.WEEKLY)
        val updateHabit2 = habit2.copy(name = "Reading books")
        habitDao.update(updateHabit1)
        habitDao.update(updateHabit2)
        val habits = habitDao.getAllHabits().first()
        assertEquals(habits[0], updateHabit1)
        assertEquals(habits[1], updateHabit2)
    }

    private suspend fun addOneHabitToDb() {
        habitDao.insert(habit1)
    }

    private suspend fun addTwoHabitsToDb() {
        habitDao.insert(habit1)
        habitDao.insert(habit2)
    }

}