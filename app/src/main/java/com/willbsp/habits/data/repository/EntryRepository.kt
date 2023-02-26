package com.willbsp.habits.data.repository

import com.willbsp.habits.data.model.Entry
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface EntryRepository {

    fun getEntries(): Flow<List<Entry>>
    fun getEntries(habitId: Int): Flow<List<Entry>>
    suspend fun getEntries(date: LocalDate, habitId: Int): Entry?
    suspend fun getOldestEntry(habitId: Int): Entry?
    suspend fun toggleEntry(habitId: Int, date: LocalDate)

}