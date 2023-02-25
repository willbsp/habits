package com.willbsp.habits.data.repo

import com.willbsp.habits.data.database.dao.HabitWithEntriesDao
import com.willbsp.habits.data.model.HabitWithEntries
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class LocalHabitWithEntriesRepository @Inject constructor(
    private val habitWithEntriesDao: HabitWithEntriesDao
) : HabitWithEntriesRepository {

    override fun getHabitsWithEntries(): Flow<List<HabitWithEntries>> {
        return habitWithEntriesDao.getHabitsWithEntries()
    }

    override fun getHabitsWithEntries(date: LocalDate): Flow<List<HabitWithEntries>> {
        return habitWithEntriesDao.getHabitsWithEntries(date)
    }

}