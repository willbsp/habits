package com.willbsp.habits.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.willbsp.habits.data.Entry
import com.willbsp.habits.data.Habit
import kotlinx.coroutines.flow.Flow

@Dao
interface EntryDao {

    @Query("SELECT * from entries ORDER BY id ASC")
    fun getAllEntries(): Flow<List<Entry>>

    @Insert(onConflict = OnConflictStrategy.IGNORE) // TODO will need to change this
    suspend fun insert(entry: Entry)

    @Update
    suspend fun update(entry: Entry)

    @Delete
    suspend fun delete(entry: Entry)

}