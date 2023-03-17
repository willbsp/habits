package com.willbsp.habits.ui.screens.logbook

import java.time.LocalDate

sealed class LogbookUiState {

    object NoHabits : LogbookUiState()

    data class SelectedHabit(
        val selectedHabitId: Int,
        val selectedHabitDates: List<LocalDate>,
        val habits: List<Habit>
    ) : LogbookUiState()

    data class Habit(
        val id: Int,
        val name: String
    )

}