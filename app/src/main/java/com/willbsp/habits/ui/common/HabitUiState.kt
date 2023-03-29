package com.willbsp.habits.ui.common

import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.model.HabitFrequency

sealed class HabitUiState {

    object Loading : HabitUiState()

    data class Habit(
        val name: String = "",
        val nameIsInvalid: Boolean = false,
        val frequency: HabitFrequency = HabitFrequency.DAILY,
        val repeat: Int? = null
    ) : HabitUiState()

}


fun HabitUiState.Habit.toHabit(id: Int? = null): Habit {
    return if (id != null) Habit(id = id, name = this.name, frequency = this.frequency)
    else Habit(name = this.name, frequency = this.frequency)
}