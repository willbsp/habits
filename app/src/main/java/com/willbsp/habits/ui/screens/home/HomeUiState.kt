package com.willbsp.habits.ui.screens.home

import com.willbsp.habits.common.rangeTo
import com.willbsp.habits.data.model.HabitFrequency
import java.time.DayOfWeek
import java.time.LocalDate

sealed class HomeUiState {

    object Empty : HomeUiState()

    data class Habits(
        val habits: List<Habit>,
        val showStreaks: Boolean,
        val showSubtitle: Boolean
    ) : HomeUiState()

    data class Habit(
        val id: Int,
        val name: String,
        val type: HabitFrequency,
        val streak: Int?,
        val completed: List<LocalDate>,
        val completedByWeek: List<LocalDate>
    ) {

        fun hasBeenCompleted(date: LocalDate = LocalDate.now()): Boolean {
            return when (type) {

                HabitFrequency.DAILY -> {
                    completed.contains(date)
                }

                HabitFrequency.WEEKLY -> {
                    val weekDates =
                        (date.with(DayOfWeek.MONDAY)..date.with(DayOfWeek.SUNDAY)).toList()
                    (completed + completedByWeek).containsAll(weekDates)
                }

            }
        }

    }

}
