package com.willbsp.habits.ui.screens.logbook

import com.willbsp.habits.data.model.Habit

data class LogbookUiState(
    val habits: List<LogbookHabitUiState> = listOf()
)

data class LogbookHabitUiState(
    val habit: Habit,
    val completed: Boolean
)