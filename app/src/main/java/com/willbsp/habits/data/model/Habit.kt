package com.willbsp.habits.data.model

import androidx.annotation.StringRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.willbsp.habits.R

enum class HabitFrequency(@StringRes val userReadableStringRes: Int) {
    DAILY(R.string.frequency_daily),
    WEEKLY(R.string.frequency_weekly)
}

@Entity(tableName = "habit")
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val frequency: HabitFrequency,
    val repeat: Int? = null
)
