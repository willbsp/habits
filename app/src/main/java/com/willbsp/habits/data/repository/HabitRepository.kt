package com.willbsp.habits.data.repository

import com.willbsp.habits.data.model.Habit
import kotlinx.coroutines.flow.Flow

interface HabitRepository {

    fun getAllHabitsStream(): Flow<List<Habit>>
    fun getHabitStream(habitId: Int): Flow<Habit?>
    suspend fun getHabit(habitId: Int): Habit?
    suspend fun insertHabit(habit: Habit): Long
    suspend fun upsertHabit(habit: Habit)
    suspend fun deleteHabit(habitId: Int)

}