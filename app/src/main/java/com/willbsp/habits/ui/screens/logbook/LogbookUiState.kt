package com.willbsp.habits.ui.screens.logbook

data class LogbookUiState(
    val habits: List<LogbookHabitUiState> = listOf()
)

data class LogbookHabitUiState(
    val id: Int,
    val name: String,
    val completed: Boolean
)