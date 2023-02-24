package com.willbsp.habits.data.repo

import com.willbsp.habits.data.model.Entry
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface EntryRepository {

    fun getAllEntries(): Flow<List<Entry>>
    fun getEntriesForHabit(habitId: Int): Flow<List<Entry>>
    suspend fun getEntryForDate(date: LocalDate, habitId: Int): Entry?
    suspend fun getOldestEntry(habitId: Int): Entry?
    suspend fun toggleEntry(habitId: Int, date: LocalDate)

}