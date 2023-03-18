package com.willbsp.habits.ui.common

import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.model.HabitFrequency

data class ModifyHabitUiState(
    val name: String = "",
    val nameIsInvalid: Boolean = false,
    val frequency: HabitFrequency = HabitFrequency.DAILY,
)

fun ModifyHabitUiState.toHabit(id: Int? = null): Habit {
    return if (id != null) Habit(id = id, name = this.name, frequency = this.frequency)
    else Habit(name = this.name, frequency = this.frequency)
}