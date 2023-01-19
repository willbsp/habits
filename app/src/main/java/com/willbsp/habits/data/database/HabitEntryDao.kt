package com.willbsp.habits.data.database

import androidx.room.Dao
import androidx.room.Query
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
    fun getTodayHabitEntries(date: String): Flow<List<HabitEntry>>

}

// TODO TODO TODO move to another file
data class HabitEntry(val habitId: Int, val habitName: String, val completed: Boolean)