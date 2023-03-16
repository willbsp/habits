package com.willbsp.habits.ui.screens.logbook

import java.time.LocalDate

sealed class LogbookCalendarUiState {

    object NoSelection : LogbookCalendarUiState()

    data class SelectedHabit(
        val selectedHabitDates: List<LocalDate>
    ) : LogbookCalendarUiState()

}

sealed class LogbookBottomSheetUiState {

    object NoHabits : LogbookBottomSheetUiState()

    data class Habits(
        val habits: List<Habit>
    ) : LogbookBottomSheetUiState()

    data class Habit(
        val id: Int,
        val name: String
    )

}