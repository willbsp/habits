package com.willbsp.habits.domain.model

import com.willbsp.habits.data.model.Habit

data class HabitWithVirtualEntries(
    val habit: Habit,
    val entries: List<VirtualEntry>
)