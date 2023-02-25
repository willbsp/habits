package com.willbsp.habits.data.repo

import com.willbsp.habits.data.model.HabitWithEntries
import kotlinx.coroutines.flow.Flow

interface HabitWithEntriesRepository {

    fun getHabitsWithEntries(): Flow<List<HabitWithEntries>>

}