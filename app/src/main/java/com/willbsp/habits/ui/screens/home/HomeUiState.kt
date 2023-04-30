package com.willbsp.habits.ui.screens.home

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
        val streak: Int?,
        val dates: List<LocalDate>,
        val datesByWeek: List<LocalDate>
    )

}
