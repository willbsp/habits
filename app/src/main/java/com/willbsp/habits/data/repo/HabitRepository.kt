package com.willbsp.habits.data.repo

import com.willbsp.habits.data.model.Entry
import com.willbsp.habits.data.model.Habit
import kotlinx.coroutines.flow.Flow

interface HabitRepository {

    fun getHabitsCompletedForDateStream(date: String): Flow<List<Pair<Habit, Boolean>>>

    fun getHabitsCompletedForDatesStream(dates: List<String>): Flow<List<Pair<Habit, List<Pair<String, Boolean>>>>>

    suspend fun getHabitById(id: Int): Habit

    suspend fun getEntryForDate(date: String, habitId: Int): Entry?

    suspend fun addHabit(habit: Habit)

    suspend fun updateHabit(habit: Habit)

    suspend fun toggleEntry(habitId: Int, date: String)

    suspend fun deleteHabit(habit: Habit)

}