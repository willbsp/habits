package com.willbsp.habits.data.database.dao

import androidx.room.*
import com.willbsp.habits.data.model.Entry
import java.time.LocalDate

@Dao
interface EntryDao {

    @Query("SELECT * FROM entries WHERE date = :date AND habit_id = :habitId")
    suspend fun getEntryForDate(date: LocalDate, habitId: Int): Entry?

    @Insert(onConflict = OnConflictStrategy.IGNORE) // TODO will need to change this
    suspend fun insert(entry: Entry)

    @Update
    suspend fun update(entry: Entry)

    @Delete
    suspend fun delete(entry: Entry)

}