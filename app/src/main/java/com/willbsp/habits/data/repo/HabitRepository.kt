package com.willbsp.habits.data.repo

import com.willbsp.habits.data.model.Entry
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.model.HabitEntry
import kotlinx.coroutines.flow.Flow

interface HabitRepository {

    fun getHabitEntriesForDateStream(date: String): Flow<List<HabitEntry>>

    suspend fun getEntryForDate(date: String, habitId: Int): Entry?

    suspend fun addHabit(habit: Habit)

    suspend fun toggleEntry(habitId: Int, date: String)

    suspend fun deleteHabit(habit: Habit)

}