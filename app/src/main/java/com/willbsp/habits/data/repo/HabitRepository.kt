package com.willbsp.habits.data.repo

import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.model.HabitWithEntries
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface HabitRepository {

    fun getAllHabitsWithEntriesForDates(dates: List<LocalDate>): Flow<List<HabitWithEntries>>

    suspend fun getHabitById(habitId: Int): Habit

    suspend fun addHabit(habit: Habit)

    suspend fun updateHabit(habit: Habit)

    suspend fun deleteHabit(habitId: Int)

}