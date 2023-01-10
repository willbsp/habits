package com.willbsp.habits.data.repo

import com.willbsp.habits.data.model.Entry
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.model.HabitWithEntries
import kotlinx.coroutines.flow.Flow

interface HabitRepository {

    fun getAllHabitsStream(): Flow<List<Habit>>

    fun entryExistsForDateStream(date: String, habitId: Int): Flow<Boolean>

    fun entryExistsForDate(date: String, habitId: Int): Flow<Boolean>

    suspend fun insertEntry(entry: Entry)

    suspend fun insertHabit(habit: Habit)

    suspend fun deleteHabit(habit: Habit)

    suspend fun updateHabit(habit: Habit)

}