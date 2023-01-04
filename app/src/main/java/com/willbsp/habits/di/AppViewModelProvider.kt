package com.willbsp.habits.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.willbsp.habits.ui.HabitViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HabitViewModel(
                (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HabitsApplication)
                    .container
                    .habitsRepository
            )
        }
    }
}