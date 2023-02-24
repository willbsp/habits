package com.willbsp.habits.data.repo

import com.willbsp.habits.data.database.dao.EntryDao
import com.willbsp.habits.data.model.Entry
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class LocalEntryRepository @Inject constructor(
    private val entryDao: EntryDao
) : EntryRepository {

    override fun getAllEntries(): Flow<List<Entry>> {
        return entryDao.getAllEntries()
    }

    override fun getEntriesForHabit(habitId: Int): Flow<List<Entry>> {
        return entryDao.getEntriesForHabit(habitId)
    }

    override suspend fun getEntryForDate(date: LocalDate, habitId: Int): Entry? {
        return entryDao.getEntryForDate(habitId, date)
    }

    override suspend fun getOldestEntry(habitId: Int): Entry? {
        return entryDao.getOldestEntry(habitId)
    }

    override suspend fun toggleEntry(
        habitId: Int,
        date: LocalDate
    ) { // TODO check if habit exists first!
        val entry: Entry? = entryDao.getEntryForDate(habitId, date)
        if (entry == null) entryDao.insert(Entry(habitId = habitId, date = date))
        else entryDao.delete(entry)
    }

}