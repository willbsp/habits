package com.willbsp.habits.domain.model

import com.willbsp.habits.data.model.Entry
import com.willbsp.habits.data.model.Habit

data class HabitWithEntries(
    val habit: Habit,
    val entries: List<Entry>
)