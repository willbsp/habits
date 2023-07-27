package com.willbsp.habits.ui.common

import androidx.annotation.StringRes
import com.willbsp.habits.R
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.model.HabitFrequency
import java.time.LocalTime

sealed class HabitUiState {

    object Loading : HabitUiState()

    data class Habit(
        val name: String = "",
        val nameIsInvalid: Boolean = false,
        val frequency: HabitFrequency = HabitFrequency.DAILY,
        val repeat: Int = 1,
        val reminderType: HabitReminderTypes = HabitReminderTypes.NONE,
        val reminderTime: LocalTime = LocalTime.of(12, 0),
        val reminderDays: List<Int> = listOf()
    ) : HabitUiState()

}

enum class HabitReminderTypes(@StringRes val userReadableStringRes: Int) {
    NONE(R.string.modify_reminder_none),
    EVERYDAY(R.string.modify_reminder_every_day),
    SPECIFIC(R.string.modify_reminder_specific_days)
}

fun HabitUiState.Habit.toHabit(id: Int? = null): Habit {
    return if (id != null) Habit(
        id = id,
        name = this.name,
        frequency = this.frequency,
        repeat = this.repeat
    )
    else Habit(name = this.name, frequency = this.frequency, repeat = this.repeat)
}