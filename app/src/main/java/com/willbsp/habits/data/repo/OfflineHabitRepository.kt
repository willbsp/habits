package com.willbsp.habits.data.repo

import com.willbsp.habits.data.database.EntryDao
import com.willbsp.habits.data.database.HabitDao
import com.willbsp.habits.data.model.Entry
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.model.HabitWithEntries
import kotlinx.coroutines.flow.Flow

class OfflineHabitRepository(
    private val habitDao: HabitDao,
    private val entryDao: EntryDao
) : HabitRepository {

    override fun getAllHabitsStream(): Flow<List<Habit>> {
        return habitDao.getAllHabits()
    }

    override fun getAllHabitsWithEntriesStream(): Flow<List<HabitWithEntries>> {
        return habitDao.getHabitsWithEntries()
    }

    override suspend fun insertEntry(entry: Entry) {
        entryDao.insert(entry)
    }

    override suspend fun insertHabit(habit: Habit) {
        habitDao.insert(habit)
    }

    override suspend fun deleteHabit(habit: Habit) {
        habitDao.delete(habit)
    }

    override suspend fun updateHabit(habit: Habit) {
        habitDao.update(habit)
    }

}