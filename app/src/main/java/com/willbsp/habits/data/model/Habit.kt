package com.willbsp.habits.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habit")
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val frequency: HabitFrequency,
    val repeat: Int? = null
)
