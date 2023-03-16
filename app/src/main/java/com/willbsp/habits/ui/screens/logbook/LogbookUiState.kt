package com.willbsp.habits.ui.screens.logbook

import java.time.LocalDate

sealed class LogbookUiState {

    object Empty : LogbookUiState()

    object NoSelection : LogbookUiState()

    data class Dates(
        val completedDates: List<LocalDate>
    ) : LogbookUiState()

}