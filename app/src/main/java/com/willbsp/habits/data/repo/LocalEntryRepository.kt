package com.willbsp.habits.data.repo

import com.willbsp.habits.data.database.dao.EntryDao
import com.willbsp.habits.data.model.Entry
import java.time.LocalDate
import javax.inject.Inject

class LocalEntryRepository @Inject constructor(
    private val entryDao: EntryDao
) : EntryRepository {

    override suspend fun getEntryForDate(date: LocalDate, habitId: Int): Entry? {
        return entryDao.getEntryForDate(date, habitId)
    }

    override suspend fun toggleEntry(
        habitId: Int,
        date: LocalDate
    ) { // TODO check if habit exists first!

        val entry: Entry? = entryDao.getEntryForDate(date, habitId)
        if (entry == null) entryDao.insert(
            Entry(
                habitId = habitId,
                date = date
            )
        )
        else entryDao.delete(entry)

    }

}