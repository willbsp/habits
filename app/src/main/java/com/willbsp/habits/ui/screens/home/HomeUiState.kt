package com.willbsp.habits.ui.screens.home

import java.time.LocalDate

sealed class HomeUiState {

    object Empty : HomeUiState()

    data class Habits(
        val habits: List<Habit>
    ) : HomeUiState()

    data class Habit(
        val id: Int,
        val name: String,
        val streak: Int?,
        val dates: List<LocalDate>
    )

}
