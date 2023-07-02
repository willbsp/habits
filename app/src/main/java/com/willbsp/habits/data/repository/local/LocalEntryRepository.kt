package com.willbsp.habits.data.repository.local

import com.willbsp.habits.data.database.dao.EntryDao
import com.willbsp.habits.data.model.Entry
import com.willbsp.habits.data.repository.EntryRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class LocalEntryRepository @Inject constructor(
    private val entryDao: EntryDao
) : EntryRepository {

    override fun getAllEntriesStream(): Flow<List<Entry>> = entryDao.getAllEntries()

    override fun getAllEntriesStream(habitId: Int): Flow<List<Entry>> =
        entryDao.getEntriesForHabit(habitId)

    override suspend fun getEntry(date: LocalDate, habitId: Int): Entry? =
        entryDao.getEntryForDate(habitId, date)

    override suspend fun getOldestEntry(habitId: Int): Entry? = entryDao.getOldestEntry(habitId)

    override suspend fun toggleEntry(
        habitId: Int,
        date: LocalDate
    ) {
        val entry: Entry? = entryDao.getEntryForDate(habitId, date)
        if (entry == null) entryDao.insert(Entry(habitId = habitId, date = date))
        else entryDao.delete(entry)
    }

}