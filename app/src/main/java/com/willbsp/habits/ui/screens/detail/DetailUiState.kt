package com.willbsp.habits.ui.screens.detail

import com.willbsp.habits.data.model.HabitFrequency
import java.time.DayOfWeek
import java.time.LocalDate

data class DetailUiState(
    val habitId: Int,
    val habitName: String = "",
    val type: HabitFrequency = HabitFrequency.DAILY,
    val repeat: Int = 0,
    val streak: Int = 0,
    val longestStreak: Int = 0,
    val started: LocalDate? = null,
    val total: Int = 0,
    val score: Int = 0,
    val reminderDays: List<DayOfWeek> = listOf()
)
