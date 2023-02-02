package com.willbsp.habits.data.repo

import com.willbsp.habits.data.database.dao.EntryDao
import com.willbsp.habits.data.database.dao.HabitDao
import com.willbsp.habits.data.model.Entry
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.model.HabitWithEntries
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class LocalHabitRepository @Inject constructor(
    private val habitDao: HabitDao,
    private val entryDao: EntryDao
) : HabitRepository {

    override fun getAllHabitsWithEntriesForDates(dates: List<LocalDate>): Flow<List<HabitWithEntries>> {
        return habitDao.getAllHabitsWithEntries()
    }

    override suspend fun getHabitById(habitId: Int): Habit {
        val habit = habitDao.getHabitById(habitId)
        return Habit(habit.id, habit.name, habit.frequency)
    }

    override suspend fun getEntryForDate(date: LocalDate, habitId: Int): Entry? {
        return entryDao.getEntryForDate(date, habitId)
    }

    override suspend fun addHabit(habit: Habit) {
        habitDao.insert(Habit(habit.id, habit.name, habit.frequency))
    }

    override suspend fun updateHabit(habit: Habit) {
        habitDao.update(Habit(habit.id, habit.name, habit.frequency))
    }

    override suspend fun toggleEntry(
        habitId: Int,
        date: LocalDate
    ) { // TODO create exception if habit does not exist

        val entry: Entry? =
            entryDao.getEntryForDate(date, habitId)
        if (entry == null) entryDao.insert(
            Entry(
                habitId = habitId,
                date = date
            )
        )
        else entryDao.delete(entry)

    }

    override suspend fun deleteHabit(habitId: Int) {
        habitDao.delete(habitDao.getHabitById(habitId))
    }

}