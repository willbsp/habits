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
import com.willbsp.habits.data.model.Entry
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
open class EntryDaoTest {

    lateinit var entryDao: EntryDao
    lateinit var habitDao: HabitDao
    private lateinit var habitDatabase: HabitDatabase

    @Before
    open fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        habitDatabase = Room.inMemoryDatabaseBuilder(context, HabitDatabase::class.java).build()
        entryDao = habitDatabase.entryDao()
        habitDao = habitDatabase.habitDao()
    }

    @After
    @Throws(IOException::class)
    open fun closeDb() {
        habitDatabase.close()
    }

    @Test
    fun getAllEntries_whenNoEntries_returnsEmptyList() = runTest {
        assertEquals(listOf<Entry>(), entryDao.getAllEntriesStream().first())
    }

    @Test
    fun getAllEntries_whenEntries_returnsEntries() = runTest {
        habitDao.upsert(habit2)
        entryDao.insert(entry2)
        assertEquals(listOf(entry2), entryDao.getAllEntriesStream().first())
        habitDao.upsert(habit3)
        entryDao.insert(entry3)
        assertEquals(listOf(entry2, entry3), entryDao.getAllEntriesStream().first())
    }

    @Test
    fun getEntriesForHabit_whenNoEntries_returnsEmptyList() = runTest {
        assertEquals(listOf<Entry>(), entryDao.getAllEntriesStream(habit2.id).first())
    }

    @Test
    fun getEntriesForHabit_whenEntries_returnsEntries() = runTest {
        habitDao.upsert(habit2)
        entryDao.insert(entry2)
        assertEquals(listOf(entry2), entryDao.getAllEntriesStream(habit2.id).first())
    }

    @Test
    fun getEntryForDate_whenNoEntryExists_returnNull() = runTest {
        assertNull(entryDao.getEntryForDate(habit2.id, entry2.date))
    }

    @Test
    fun getEntryForDate_whenEntryExists_returnEntry() = runTest {
        habitDao.upsert(habit2)
        entryDao.insert(entry2)
        assertEquals(entry2, entryDao.getEntryForDate(habit2.id, entry2.date))
    }

    @Test
    fun getOldestEntry_whenNoEntries_returnNull() = runTest {
        assertNull(entryDao.getOldestEntry(habit2.id))
    }

    @Test
    fun getOldestEntry_whenEntries_returnsOldest() = runTest {
        habitDao.upsert(habit2)
        entryDao.insert(entry2)
        entryDao.insert(entry2.copy(id = entry2.id + 1, date = entry2.date.minusDays(3)))
        val expected = entry2.copy(id = entry2.id + 2, date = entry2.date.minusWeeks(3))
        entryDao.insert(expected)
        assertEquals(expected, entryDao.getOldestEntry(habit2.id))
    }

    @Test
    fun insert_whenEntryInserted_addedToDb() = runTest {
        habitDao.upsert(habit2)
        entryDao.insert(entry2)
        assertEquals(entry2, entryDao.getEntryForDate(entry2.habitId, entry2.date))
    }

    @Test
    fun delete_whenEntryDeleted_removedFromDb() = runTest {
        habitDao.upsert(habit2)
        entryDao.insert(entry2)
        assertEquals(entry2, entryDao.getEntryForDate(entry2.habitId, entry2.date))
        entryDao.delete(entry2)
        assertNull(entryDao.getEntryForDate(entry2.habitId, entry2.date))
    }

}

class FakeEntryDaoTest : EntryDaoTest() {

    @Before
    override fun createDb() {
        habitDao = FakeHabitDao()
        entryDao = FakeEntryDao()
    }

    @After
    override fun closeDb() {
    }

}
