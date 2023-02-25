package com.willbsp.habits.data.repo

import com.willbsp.habits.data.model.HabitWithEntries
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface HabitWithEntriesRepository {

    fun getHabitsWithEntries(): Flow<List<HabitWithEntries>>
    fun getHabitsWithEntries(date: LocalDate): Flow<List<HabitWithEntries>>

}