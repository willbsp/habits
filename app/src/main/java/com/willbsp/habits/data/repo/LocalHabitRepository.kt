package com.willbsp.habits.data.repo

import com.willbsp.habits.data.database.dao.HabitDao
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.model.HabitWithEntries
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class LocalHabitRepository @Inject constructor(
    private val habitDao: HabitDao
) : HabitRepository {

    override fun getAllHabitsWithEntriesForDates(dates: List<LocalDate>): Flow<List<HabitWithEntries>> {
        return habitDao.getAllHabitsWithEntries()
    }

    override suspend fun getHabitById(habitId: Int): Habit {
        val habit = habitDao.getHabitById(habitId)
        return Habit(habit.id, habit.name, habit.frequency)
    }

    override suspend fun addHabit(habit: Habit) {
        habitDao.insert(Habit(habit.id, habit.name, habit.frequency))
    }

    override suspend fun updateHabit(habit: Habit) {
        habitDao.update(Habit(habit.id, habit.name, habit.frequency))
    }

    override suspend fun deleteHabit(habitId: Int) {
        habitDao.delete(habitDao.getHabitById(habitId))
    }

}