package com.willbsp.habits.data.database

import androidx.room.*
import com.willbsp.habits.data.model.Entry
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