package com.willbsp.habits.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.willbsp.habits.data.model.HabitWithEntries
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface HabitWithEntriesDao {

    @Transaction
    @Query("SELECT * FROM habit")
    fun getHabitsWithEntries(): Flow<List<HabitWithEntries>>

    @Transaction
    @Query("SELECT * FROM habit, entries WHERE habit.id = entries.habit_id AND entries.date = :date")
    fun getHabitsWithEntries(date: LocalDate): Flow<List<HabitWithEntries>>

}