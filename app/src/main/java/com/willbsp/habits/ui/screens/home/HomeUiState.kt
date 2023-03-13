package com.willbsp.habits.ui.screens.home

import java.time.LocalDate

sealed class HomeUiState {
    object Empty : HomeUiState()
    data class Habits(
        val habits: List<HomeHabitUiState>
    ) : HomeUiState()
}

data class HomeHabitUiState(
    val id: Int,
    val name: String,
    val streak: Int?,
    val dates: List<LocalDate>
)