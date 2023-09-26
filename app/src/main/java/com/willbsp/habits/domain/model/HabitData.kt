package com.willbsp.habits.domain.model

import androidx.annotation.StringRes
import com.willbsp.habits.R
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.model.HabitFrequency
import java.time.DayOfWeek
import java.time.LocalTime

data class HabitData(
    val name: String = "",
    val frequency: HabitFrequency = HabitFrequency.DAILY,
    val repeat: Int = 1,
    val reminderType: HabitReminderType = HabitReminderType.NONE,
    val reminderTime: LocalTime = LocalTime.NOON,
    val reminderDays: Set<DayOfWeek> = setOf()
)

fun HabitData.toHabit(id: Int? = null): Habit {
    return if (id != null) Habit(
        id = id,
        name = this.name,
        frequency = this.frequency,
        repeat = this.repeat
    )
    else Habit(name = this.name, frequency = this.frequency, repeat = this.repeat)
}

// TODO depends on platform code!
enum class HabitReminderType(@StringRes val userReadableStringRes: Int) {
    NONE(R.string.modify_reminder_none),
    EVERYDAY(R.string.modify_reminder_every_day),
    SPECIFIC(R.string.modify_reminder_specific_days)
}
