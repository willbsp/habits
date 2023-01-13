package com.willbsp.habits.data.database

import androidx.room.*
import com.willbsp.habits.data.model.Entry
import kotlinx.coroutines.flow.Flow

@Dao
interface EntryDao {

    @Query("SELECT EXISTS(SELECT * FROM entries WHERE date = :date AND habit_id = :habitId)")
    fun entryExistsForDate(date: String, habitId: Int): Flow<Boolean>

    @Query("SELECT * FROM entries WHERE date = :date AND habit_id = :habitId")
    suspend fun getEntryForDate(date: String, habitId: Int): Entry?

    @Insert(onConflict = OnConflictStrategy.IGNORE) // TODO will need to change this
    suspend fun insert(entry: Entry)

    @Update
    suspend fun update(entry: Entry)

    @Delete
    suspend fun delete(entry: Entry)

}