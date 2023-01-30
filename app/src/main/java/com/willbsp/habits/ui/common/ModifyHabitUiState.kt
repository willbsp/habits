package com.willbsp.habits.ui.common

import com.willbsp.habits.data.model.HabitFrequency

data class ModifyHabitUiState(
    val name: String = "",
    val frequency: HabitFrequency = HabitFrequency.DAILY
)