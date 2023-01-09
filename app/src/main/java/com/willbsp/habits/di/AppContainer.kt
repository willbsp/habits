package com.willbsp.habits.di

import android.content.Context
import com.willbsp.habits.data.database.HabitDatabase
import com.willbsp.habits.data.repo.HabitRepository
import com.willbsp.habits.data.repo.OfflineHabitRepository

interface AppContainer {
    val habitsRepository: HabitRepository
}

// TODO look at inventory example for examples of code comments
class AppDataContainer(private val context: Context) : AppContainer {
    override val habitsRepository: HabitRepository by lazy {
        OfflineHabitRepository(
            habitDao = HabitDatabase.getDatabase(context).habitDao(),
            entryDao = HabitDatabase.getDatabase(context).entryDao()
        )
    }
}