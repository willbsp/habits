package com.willbsp.habits.ui.screens.home

import java.time.LocalDate

enum class HabitState {
    SHOW_HABITS,
    ALL_COMPLETED,
    NO_HABITS
}

data class HomeUiState(
    val todayState: List<HomeHabitUiState> = listOf(),
    val completedCount: Int = 0,
    val habitState: HabitState = HabitState.NO_HABITS
)

data class HomeHabitUiState(
    val id: Int,
    val name: String,
    val streak: Int?,
    val completedDates: List<HomeCompletedUiState>
)

data class HomeCompletedUiState(
    val date: LocalDate,
    val completed: Boolean
)