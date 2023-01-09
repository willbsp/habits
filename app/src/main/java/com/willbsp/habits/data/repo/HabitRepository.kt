package com.willbsp.habits.data.repo

import com.willbsp.habits.data.model.Habit
import kotlinx.coroutines.flow.Flow

interface HabitRepository {

    fun getAllHabitsStream(): Flow<List<Habit>>

    suspend fun insertHabit(habit: Habit)

    suspend fun deleteHabit(habit: Habit)

    suspend fun updateHabit(habit: Habit)

}