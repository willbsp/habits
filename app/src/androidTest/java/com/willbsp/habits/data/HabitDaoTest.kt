package com.willbsp.habits.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.willbsp.habits.data.TestData.habit2
import com.willbsp.habits.data.TestData.habit3
import com.willbsp.habits.data.database.HabitDatabase
import com.willbsp.habits.data.database.dao.EntryDao
import com.willbsp.habits.data.database.dao.HabitDao
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.fake.dao.FakeEntryDao
import com.willbsp.habits.fake.dao.FakeHabitDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
open class HabitDaoTest {

    lateinit var habitDao: HabitDao
    lateinit var entryDao: EntryDao
    private lateinit var habitDatabase: HabitDatabase

    @Before
    open fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        habitDatabase = Room.inMemoryDatabaseBuilder(context, HabitDatabase::class.java).build()
        habitDao = habitDatabase.habitDao()
        entryDao = habitDatabase.entryDao()
    }

    @After
    @Throws(IOException::class)
    open fun closeDb() {
        habitDatabase.close()
    }

    @Test
    fun getAllHabitsStream_whenNoHabits_returnEmptyList() = runTest {
        assertEquals(listOf<Habit>(), habitDao.getAllHabitsStream().first())
    }

    @Test
    fun getAllHabitsStream_whenHabits_returnHabits() = runTest {
        habitDao.upsert(habit2)
        assertEquals(listOf(habit2), habitDao.getAllHabitsStream().first())
        habitDao.upsert(habit3)
        assertEquals(listOf(habit2, habit3), habitDao.getAllHabitsStream().first())
    }

    @Test
    fun getHabitStream_whenNoHabit_returnNull() = runTest {
        assertNull(habitDao.getHabitStream(habit2.id).first())
    }

    @Test
    fun getHabitStream_whenHabitExists_returnHabit() = runTest {
        habitDao.upsert(habit2)
        assertEquals(habit2, habitDao.getHabitStream(habit2.id).first())
    }

    @Test
    fun getHabitStream_whenHabitAdded_returnHabit() = runTest {
        assertNull(habitDao.getHabitStream(habit2.id).first())
        habitDao.upsert(habit2)
        assertEquals(habit2, habitDao.getHabitStream(habit2.id).first())
    }

    @Test
    fun getHabit_whenNoHabit_returnNull() = runTest {
        assertNull(habitDao.getHabit(habit2.id))
    }

    @Test
    fun getHabit_whenHabitExists_returnHabit() = runTest {
        habitDao.upsert(habit2)
        assertEquals(habit2, habitDao.getHabit(habit2.id))
    }

    @Test
    fun upsert_whenInserted_insertHabitInDb() = runTest {
        habitDao.upsert(habit2)
        assertEquals(habit2, habitDao.getHabit(habit2.id))
    }

    @Test
    fun upsert_whenUpdated_updateHabitInDb() = runTest {
        habitDao.upsert(habit2)
        assertEquals(habit2, habitDao.getHabit(habit2.id))
        val expected = habit2.copy(name = "newname")
        habitDao.upsert(expected)
        assertEquals(expected, habitDao.getHabit(habit2.id))
    }

    @Test
    fun delete_whenDeleted_removeHabitInDb() = runTest {
        habitDao.upsert(habit2)
        assertEquals(habit2, habitDao.getHabit(habit2.id))
        habitDao.delete(habit2)
        assertNull(habitDao.getHabit(habit2.id))
    }

}

class FakeHabitDaoTest : HabitDaoTest() {

    @Before
    override fun createDb() {
        habitDao = FakeHabitDao()
        entryDao = FakeEntryDao()
    }

    @After
    override fun closeDb() {
    }

}