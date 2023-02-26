package com.willbsp.habits.data.repository

import com.willbsp.habits.data.model.Habit
import kotlinx.coroutines.flow.Flow

interface HabitRepository {

    fun getHabits(): Flow<List<Habit>>
    suspend fun getHabitById(habitId: Int): Habit
    suspend fun addHabit(habit: Habit)
    suspend fun updateHabit(habit: Habit)
    suspend fun deleteHabit(habitId: Int)

}