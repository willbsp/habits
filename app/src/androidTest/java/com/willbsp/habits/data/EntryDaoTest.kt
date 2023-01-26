package com.willbsp.habits.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.willbsp.habits.TestData.entry1
import com.willbsp.habits.TestData.entry2
import com.willbsp.habits.TestData.habit1
import com.willbsp.habits.TestData.habit2
import com.willbsp.habits.data.database.EntryDao
import com.willbsp.habits.data.database.HabitDao
import com.willbsp.habits.data.database.HabitDatabase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class EntryDaoTest {

    private lateinit var entryDao: EntryDao
    private lateinit var habitDao: HabitDao
    private lateinit var habitDatabase: HabitDatabase


    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        habitDatabase = Room.inMemoryDatabaseBuilder(context, HabitDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        entryDao = habitDatabase.entryDao()
        habitDao = habitDatabase.habitDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        habitDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun daoGetEntryForDate_returnsNull() = runBlocking {
        addOneHabitToDb()
        addOneEntryToDb()
        val actualEntry = entryDao.getEntryForDate(entry2.date, entry2.habitId)
        assertEquals(null, actualEntry)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetEntryForDate_returnsEntryForDate() = runBlocking {
        addTwoHabitsToDb()
        addTwoEntriesToDb()
        val actualEntry = entryDao.getEntryForDate(entry2.date, entry2.habitId)
        assertEquals(actualEntry, entry2)
    }

    @Test
    @Throws(Exception::class)
    fun daoInsertEntry_insertsEntryIntoDb() = runBlocking {
        addOneHabitToDb()
        addOneEntryToDb()
        val entry = entryDao.getEntryForDate(entry1.date, entry1.habitId)
        assertEquals(entry, entry1)
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteEntry_deletesEntryFromDb() = runBlocking {
        addOneHabitToDb()
        addOneEntryToDb()
        entryDao.delete(entry1)
        val entry = entryDao.getEntryForDate(entry1.date, entry1.habitId)
        assertNull(entry)
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdateEntry_updatesEntryInDb() = runBlocking {
        addTwoHabitsToDb()
        addOneEntryToDb()
        val updateEntry = entry1.copy(habitId = 2)
        entryDao.update(updateEntry)
        val entry = entryDao.getEntryForDate(updateEntry.date, updateEntry.habitId)
        assertEquals(entry, updateEntry)
    }

    private suspend fun addOneEntryToDb() {
        entryDao.insert(entry1)
    }

    private suspend fun addTwoEntriesToDb() {
        entryDao.insert(entry1)
        entryDao.insert(entry2)
    }

    private suspend fun addOneHabitToDb() {
        habitDao.insert(habit1)
    }

    private suspend fun addTwoHabitsToDb() {
        habitDao.insert(habit1)
        habitDao.insert(habit2)
    }

}