package com.willbsp.habits.data.repo

import com.willbsp.habits.data.database.EntryDao
import com.willbsp.habits.data.database.HabitDao
import com.willbsp.habits.data.database.HabitEntryDao
import com.willbsp.habits.data.model.Entry
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.model.HabitEntry
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflineHabitRepository @Inject constructor(
    private val habitDao: HabitDao,
    private val entryDao: EntryDao,
    private val habitEntryDao: HabitEntryDao
) : HabitRepository {

    override fun getHabitEntriesForDateStream(date: String): Flow<List<HabitEntry>> {
        return habitEntryDao.getHabitEntriesForDate(date)
    }

    override suspend fun getEntryForDate(date: String, habitId: Int): Entry? {
        return entryDao.getEntryForDate(date, habitId)
    }

    override suspend fun addHabit(habit: Habit) {
        habitDao.insert(habit)
    }

    override suspend fun toggleEntry(
        habitId: Int,
        date: String
    ) { // TODO create exception if habit does not exist
        val entry: Entry? = entryDao.getEntryForDate(date, habitId)
        if (entry == null) entryDao.insert(Entry(habitId = habitId, date = date))
        else entryDao.delete(entry)
    }

    override suspend fun deleteHabit(habit: Habit) {
        habitDao.delete(habit)
    }

}