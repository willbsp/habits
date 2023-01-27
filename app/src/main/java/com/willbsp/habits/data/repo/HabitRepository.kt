package com.willbsp.habits.data.repo

import com.willbsp.habits.data.model.Entry
import com.willbsp.habits.data.model.Habit
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface HabitRepository {

    fun getHabitsCompletedForDateStream(date: LocalDate): Flow<List<Pair<Habit, Boolean>>>

    fun getHabitsCompletedForDatesStream(dates: List<LocalDate>): Flow<List<Pair<Habit, List<Pair<LocalDate, Boolean>>>>>

    suspend fun getHabitById(id: Int): Habit

    suspend fun getEntryForDate(date: LocalDate, habitId: Int): Entry?

    suspend fun addHabit(habit: Habit)

    suspend fun updateHabit(habit: Habit)

    suspend fun toggleEntry(habitId: Int, date: LocalDate)

    suspend fun deleteHabit(habit: Habit)

}