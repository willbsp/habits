package com.willbsp.habits.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.willbsp.habits.data.TestData.entry2
import com.willbsp.habits.data.TestData.entry3
import com.willbsp.habits.data.TestData.habit2
import com.willbsp.habits.data.TestData.habit3
import com.willbsp.habits.data.database.HabitDatabase
import com.willbsp.habits.data.database.dao.EntryDao
import com.willbsp.habits.data.database.dao.HabitDao
import com.willbsp.habits.data.database.dao.HabitWithEntriesDao
import com.willbsp.habits.data.model.HabitWithEntries
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
class HabitWithEntriesDaoTest {

    private lateinit var habitDao: HabitDao
    private lateinit var entryDao: EntryDao
    private lateinit var habitWithEntriesDao: HabitWithEntriesDao
    private lateinit var habitDatabase: HabitDatabase

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        habitDatabase = Room.inMemoryDatabaseBuilder(context, HabitDatabase::class.java).build()
        habitDao = habitDatabase.habitDao()
        entryDao = habitDatabase.entryDao()
        habitWithEntriesDao = habitDatabase.habitWithEntriesDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        habitDatabase.close()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getHabitsWithEntries_whenNoHabits_returnEmptyList() = runTest {
        assertEquals(listOf<HabitWithEntries>(), habitWithEntriesDao.getHabitsWithEntries().first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getHabitsWithEntries_whenHabits_returnHabitsList() = runTest {
        habitDao.upsert(habit2)
        assertEquals(
            listOf(HabitWithEntries(habit2, listOf())),
            habitWithEntriesDao.getHabitsWithEntries().first()
        )
        habitDao.upsert(habit3)
        assertEquals(
            listOf(HabitWithEntries(habit2, listOf()), HabitWithEntries(habit3, listOf())),
            habitWithEntriesDao.getHabitsWithEntries().first()
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getHabitsWithEntries_whenHabitsAndEntries_returnHabitsWithEntriesList() = runTest {
        habitDao.upsert(habit2)
        entryDao.insert(entry2)
        assertEquals(
            listOf(HabitWithEntries(habit2, listOf(entry2))),
            habitWithEntriesDao.getHabitsWithEntries().first()
        )
        habitDao.upsert(habit3)
        entryDao.insert(entry3)
        assertEquals(
            listOf(
                HabitWithEntries(habit2, listOf(entry2)),
                HabitWithEntries(habit3, listOf(entry3))
            ),
            habitWithEntriesDao.getHabitsWithEntries().first()
        )
    }

}