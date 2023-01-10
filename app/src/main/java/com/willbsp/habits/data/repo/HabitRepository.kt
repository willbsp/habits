package com.willbsp.habits.data.repo

import com.willbsp.habits.data.model.Entry
import com.willbsp.habits.data.model.Habit
import kotlinx.coroutines.flow.Flow

interface HabitRepository {

    fun getAllHabitsStream(): Flow<List<Habit>>

    fun entryExistsForDateStream(date: String, habitId: Int): Flow<Boolean>

    suspend fun getEntryForDate(date: String, habitId: Int): Entry?

    suspend fun insertHabit(habit: Habit)

    suspend fun insertEntry(entry: Entry)

    suspend fun deleteHabit(habit: Habit)

    suspend fun deleteEntry(entry: Entry)

    suspend fun updateHabit(habit: Habit)

    suspend fun updateEntry(entry: Entry)

}