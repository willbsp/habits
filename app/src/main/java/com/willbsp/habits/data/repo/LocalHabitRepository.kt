package com.willbsp.habits.data.repo

import com.willbsp.habits.data.database.dao.HabitDao
import com.willbsp.habits.data.model.Habit
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalHabitRepository @Inject constructor(
    private val habitDao: HabitDao
) : HabitRepository {

    override fun getAllHabits(): Flow<List<Habit>> {
        return habitDao.getAllHabits()
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