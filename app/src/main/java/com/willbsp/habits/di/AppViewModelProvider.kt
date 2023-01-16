package com.willbsp.habits.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.willbsp.habits.ui.add.AddHabitViewModel
import com.willbsp.habits.ui.home.HomeScreenViewModel
import java.time.Clock

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            AddHabitViewModel(
                habitsRepository = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HabitsApplication)
                    .container
                    .habitsRepository
            )
        }
        initializer {
            HomeScreenViewModel(
                habitsRepository = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HabitsApplication)
                    .container
                    .habitsRepository,
                clock = Clock.systemDefaultZone()
            )
        }
    }
}