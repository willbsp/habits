package com.willbsp.habits.data.repo

import com.willbsp.habits.data.model.Entry
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.model.HabitWithEntries
import kotlinx.coroutines.flow.Flow

interface HabitRepository {

    fun getAllHabitsStream(): Flow<List<Habit>>

    fun getAllHabitsWithEntriesStream(): Flow<List<HabitWithEntries>> // TODO not to be performed on main thread!!!

    fun entryExistsForDate(date: String, habitId: Int): Flow<Boolean>

    suspend fun insertEntry(entry: Entry)

    suspend fun insertHabit(habit: Habit)

    suspend fun deleteHabit(habit: Habit)

    suspend fun updateHabit(habit: Habit)

}