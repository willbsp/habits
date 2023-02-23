package com.willbsp.habits.data.repo

import com.willbsp.habits.data.model.Entry
import java.time.LocalDate

interface EntryRepository {

    suspend fun getEntryForDate(date: LocalDate, habitId: Int): Entry?
    suspend fun toggleEntry(habitId: Int, date: LocalDate)

}