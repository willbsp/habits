package com.willbsp.habits.data.repo

import com.willbsp.habits.data.database.HabitDao
import com.willbsp.habits.data.model.Habit
import kotlinx.coroutines.flow.Flow

class OfflineHabitRepository(private val habitDao: HabitDao) : HabitRepository {

    override fun getAllHabitsStream(): Flow<List<Habit>> {
        return habitDao.getAllHabits()
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