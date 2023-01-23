package com.willbsp.habits.data.repo

import com.willbsp.habits.data.database.EntryDao
import com.willbsp.habits.data.database.HabitDao
import com.willbsp.habits.data.model.Entry
import com.willbsp.habits.data.model.Habit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfflineHabitRepository @Inject constructor(
    private val habitDao: HabitDao,
    private val entryDao: EntryDao
) : HabitRepository {

    override fun getHabitsCompletedForDateStream(date: String): Flow<List<Pair<Habit, Boolean>>> {

        return habitDao.getAllHabitsWithEntries().map { list ->
            list.map {
                val habit = it.habit
                val completed = it.entries.any { entry -> entry.date == date }
                Pair(habit, completed)
            }
        }

    }

    override fun getHabitsCompletedForDatesStream(dates: List<String>): Flow<List<Pair<Habit, List<Pair<String, Boolean>>>>> {

        return habitDao.getAllHabitsWithEntries().map { list ->
            list.map {
                val habit = it.habit
                val completed = dates.map { date ->
                    Pair(date, it.entries.any { entry -> entry.date == date })
                }
                Pair(habit, completed)
            }
        }

    }

    override suspend fun getHabitById(id: Int): Habit {
        return habitDao.getHabitById(id)
    }

    override suspend fun getEntryForDate(date: String, habitId: Int): Entry? {
        return entryDao.getEntryForDate(date, habitId)
    }

    override suspend fun addHabit(habit: Habit) {
        habitDao.insert(habit)
    }

    override suspend fun updateHabit(habit: Habit) {
        habitDao.update(habit)
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