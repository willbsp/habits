package com.willbsp.habits.data.repo

import com.willbsp.habits.data.model.Entry
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface EntryRepository {

    fun getEntriesForHabit(habitId: Int): Flow<List<Entry>>
    suspend fun getEntryForDate(date: LocalDate, habitId: Int): Entry?
    suspend fun toggleEntry(habitId: Int, date: LocalDate)

}