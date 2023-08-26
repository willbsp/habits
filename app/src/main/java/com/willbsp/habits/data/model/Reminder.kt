package com.willbsp.habits.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.DayOfWeek
import java.time.LocalTime

@Entity(
    tableName = "reminders", foreignKeys = [ForeignKey(
        entity = Habit::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("habit_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "habit_id", index = true)
    val habitId: Int,
    val time: LocalTime,
    val day: DayOfWeek
)