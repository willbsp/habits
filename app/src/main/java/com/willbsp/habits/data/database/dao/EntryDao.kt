package com.willbsp.habits.data.database.dao

import androidx.room.*
import com.willbsp.habits.data.model.Entry
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface EntryDao {

    @Query("SELECT * FROM entries ORDER BY date DESC")
    fun getAllEntriesStream(): Flow<List<Entry>>

    @Query("SELECT * FROM entries WHERE habit_id = :habitId ORDER BY date DESC")
    fun getAllEntriesStream(habitId: Int): Flow<List<Entry>>

    @Query("SELECT * FROM entries WHERE date = :date AND habit_id = :habitId")
    suspend fun getEntryForDate(habitId: Int, date: LocalDate): Entry?

    @Query("SELECT * FROM entries WHERE habit_id = :habitId ORDER BY date ASC LIMIT 1")
    suspend fun getOldestEntry(habitId: Int): Entry?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entry: Entry)

    @Delete
    suspend fun delete(entry: Entry)

}