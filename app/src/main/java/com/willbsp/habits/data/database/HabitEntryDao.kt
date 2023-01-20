package com.willbsp.habits.data.database

import androidx.room.Dao
import androidx.room.Query
import com.willbsp.habits.data.model.HabitEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitEntryDao {

    @Query(
        "SELECT habit.id AS habitId, habit.name AS habitName, " +
                "CASE WHEN id IN (SELECT habit_id FROM entries WHERE date = :date) THEN 1 " +
                "ELSE 0 " +
                "END AS completed " +
                "FROM habit"
    )
    fun getHabitEntriesForDate(date: String): Flow<List<HabitEntry>>

}