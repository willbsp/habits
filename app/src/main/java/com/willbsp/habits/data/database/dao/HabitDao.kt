package com.willbsp.habits.data.database.dao

import androidx.room.*
import com.willbsp.habits.data.model.Habit
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Query("SELECT * FROM habit")
    fun getAllHabitsStream(): Flow<List<Habit>>

    @Query("SELECT * FROM habit WHERE id = :id")
    fun getHabitStream(id: Int): Flow<Habit?>

    @Query("SELECT * FROM habit WHERE id = :id")
    suspend fun getHabit(id: Int): Habit?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(habit: Habit)

    @Update
    suspend fun update(habit: Habit)

    @Delete
    suspend fun delete(habit: Habit)

}