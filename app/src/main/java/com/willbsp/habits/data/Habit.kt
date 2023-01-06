package com.willbsp.habits.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class HabitFrequency {
    DAILY,
    WEEKLY
}

@Entity(tableName = "habit")
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val frequency: HabitFrequency
)
