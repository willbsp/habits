package com.willbsp.habits.data.model

data class HabitWithEntries(
    val habit: Habit,
    val entries: List<Entry>
)