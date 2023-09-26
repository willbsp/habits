package com.willbsp.habits.data.repository

import com.willbsp.habits.data.model.Entry
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface EntryRepository {

    fun getAllEntriesStream(): Flow<List<Entry>>
    fun getAllEntriesStream(habitId: Int): Flow<List<Entry>>
    suspend fun getEntry(date: LocalDate, habitId: Int): Entry?
    suspend fun getOldestEntry(habitId: Int): Entry?
    suspend fun toggleEntry(habitId: Int, date: LocalDate)
    suspend fun setEntry(habitId: Int, date: LocalDate, completed: Boolean)

}