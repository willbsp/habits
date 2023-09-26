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
import com.willbsp.habits.fake.dao.FakeEntryDao
import com.willbsp.habits.fake.dao.FakeHabitDao
import com.willbsp.habits.fake.dao.FakeHabitWithEntriesDao
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
open class HabitWithEntriesDaoTest {

    lateinit var habitDao: HabitDao
    lateinit var entryDao: EntryDao
    lateinit var habitWithEntriesDao: HabitWithEntriesDao
    private lateinit var habitDatabase: HabitDatabase

    @Before
    open fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        habitDatabase = Room.inMemoryDatabaseBuilder(context, HabitDatabase::class.java).build()
        habitDao = habitDatabase.habitDao()
        entryDao = habitDatabase.entryDao()
        habitWithEntriesDao = habitDatabase.habitWithEntriesDao()
    }

    @After
    @Throws(IOException::class)
    open fun closeDb() {
        habitDatabase.close()
    }

    @Test
    fun getHabitsWithEntries_whenNoHabits_returnEmptyList() = runTest {
        assertEquals(
            listOf<HabitWithEntries>(),
            habitWithEntriesDao.getHabitsWithEntriesStream().first()
        )
    }

    @Test
    fun getHabitsWithEntries_whenHabits_returnHabitsList() = runTest {
        habitDao.upsert(habit2)
        assertEquals(
            listOf(HabitWithEntries(habit2, listOf())),
            habitWithEntriesDao.getHabitsWithEntriesStream().first()
        )
        habitDao.upsert(habit3)
        assertEquals(
            listOf(HabitWithEntries(habit2, listOf()), HabitWithEntries(habit3, listOf())),
            habitWithEntriesDao.getHabitsWithEntriesStream().first()
        )
    }

    @Test
    fun getHabitsWithEntries_whenHabitsAndEntries_returnHabitsWithEntriesList() = runTest {
        habitDao.upsert(habit2)
        entryDao.insert(entry2)
        assertEquals(
            listOf(HabitWithEntries(habit2, listOf(entry2))),
            habitWithEntriesDao.getHabitsWithEntriesStream().first()
        )
        habitDao.upsert(habit3)
        entryDao.insert(entry3)
        assertEquals(
            listOf(
                HabitWithEntries(habit2, listOf(entry2)),
                HabitWithEntries(habit3, listOf(entry3))
            ),
            habitWithEntriesDao.getHabitsWithEntriesStream().first()
        )
    }

}

class FakeHabitWithEntriesDaoTest : HabitWithEntriesDaoTest() {

    @Before
    override fun createDb() {
        habitDao = FakeHabitDao()
        entryDao = FakeEntryDao()
        habitWithEntriesDao = FakeHabitWithEntriesDao(habitDao, entryDao)
    }

    @After
    override fun closeDb() {
    }

}
