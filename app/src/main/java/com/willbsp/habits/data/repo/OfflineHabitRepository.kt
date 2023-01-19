package com.willbsp.habits.data.repo

import com.willbsp.habits.data.database.EntryDao
import com.willbsp.habits.data.database.HabitDao
import com.willbsp.habits.data.database.HabitEntryDao
import com.willbsp.habits.data.model.Entry
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.model.HabitEntry
import kotlinx.coroutines.flow.Flow
import java.time.Clock
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class OfflineHabitRepository @Inject constructor(
    private val habitDao: HabitDao,
    private val entryDao: EntryDao,
    private val habitEntryDao: HabitEntryDao,
    private val clock: Clock
) : HabitRepository {

    private fun getCurrentDate(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") // TODO make constant
        return LocalDateTime.now(clock).format(formatter)
    }

    override fun getTodaysHabitEntriesStream(): Flow<List<HabitEntry>> {
        return habitEntryDao.getTodayHabitEntries(getCurrentDate())
    }

    override suspend fun getEntryForDate(date: String, habitId: Int): Entry? {
        return entryDao.getEntryForDate(date, habitId)
    }

    override suspend fun insertHabit(habit: Habit) {
        habitDao.insert(habit)
    }

    override suspend fun insertEntry(entry: Entry) {
        entryDao.insert(entry)
    }

    override suspend fun deleteHabit(habit: Habit) {
        habitDao.delete(habit)
    }

    override suspend fun deleteEntry(entry: Entry) {
        entryDao.delete(entry)
    }

    override suspend fun updateHabit(habit: Habit) {
        habitDao.update(habit)
    }

    override suspend fun updateEntry(entry: Entry) {
        entryDao.update(entry)
    }

}