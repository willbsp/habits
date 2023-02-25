package com.willbsp.habits.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class HabitWithEntries(
    @Embedded val habit: Habit,
    @Relation(
        parentColumn = "id",
        entityColumn = "habit_id"
    )
    val entries: List<Entry>
)