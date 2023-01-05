package com.willbsp.habits.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habit")
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    var completed: Boolean // TODO needs to be completable for multiple days
)
