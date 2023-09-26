package com.willbsp.habits.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "entries", foreignKeys = [ForeignKey(
        entity = Habit::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("habit_id"),
        onDelete = ForeignKey.CASCADE,
    )]
)
data class Entry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "habit_id", index = true)
    val habitId: Int,
    @ColumnInfo(name = "date", index = true)
    val date: LocalDate,
)