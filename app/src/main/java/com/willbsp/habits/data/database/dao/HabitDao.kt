package com.willbsp.habits.data.database.dao

import androidx.room.*
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.model.HabitWithEntries
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Transaction
    @Query("SELECT * FROM habit")
    fun getAllHabitsWithEntries(): Flow<List<HabitWithEntries>>

    @Query("SELECT * FROM habit WHERE id = :id")
    suspend fun getHabitById(id: Int): Habit

    @Insert(onConflict = OnConflictStrategy.IGNORE) // TODO will need to change this
    suspend fun insert(habit: Habit)

    @Update
    suspend fun update(habit: Habit)

    @Delete
    suspend fun delete(habit: Habit)

}