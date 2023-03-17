package com.willbsp.habits.ui.screens.logbook

import java.time.LocalDate

sealed class LogbookUiState {

    object NoSelection : LogbookUiState()

    data class SelectedHabit(
        val habits: List<Habit>,
        val selectedHabitId: Int,
        val selectedHabitDates: List<LocalDate>
    ) : LogbookUiState()

    data class Habit(
        val id: Int,
        val name: String
    )

}