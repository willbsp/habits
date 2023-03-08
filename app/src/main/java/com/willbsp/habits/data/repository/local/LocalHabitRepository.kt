package com.willbsp.habits.data.repository.local

import com.willbsp.habits.data.database.dao.HabitDao
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalHabitRepository @Inject constructor(
    private val habitDao: HabitDao
) : HabitRepository {

    override fun getHabits(): Flow<List<Habit>> {
        return habitDao.getAllHabits()
    }

    override suspend fun getHabitById(habitId: Int): Habit? {
        return habitDao.getHabitById(habitId)
    }

    override suspend fun addHabit(habit: Habit) {
        habitDao.insert(Habit(habit.id, habit.name, habit.frequency))
    }

    override suspend fun updateHabit(habit: Habit) {
        habitDao.update(Habit(habit.id, habit.name, habit.frequency))
    }

    override suspend fun deleteHabit(habitId: Int) {
        val habit = habitDao.getHabitById(habitId)
        if (habit != null) {
            habitDao.delete(habit)
        }
    }

}