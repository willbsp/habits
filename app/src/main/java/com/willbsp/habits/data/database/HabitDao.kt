package com.willbsp.habits.data.database

import androidx.room.*
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.model.HabitWithEntries
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Query("SELECT * from habit")
    fun getAllHabits(): Flow<List<Habit>>

    @Transaction
    @Query("SELECT * from habit")
    fun getHabitsWithEntries(): Flow<List<HabitWithEntries>>

    @Insert(onConflict = OnConflictStrategy.IGNORE) // TODO will need to change this
    suspend fun insert(habit: Habit)

    @Update
    suspend fun update(habit: Habit)

    @Delete
    suspend fun delete(habit: Habit)

}