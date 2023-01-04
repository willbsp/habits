package com.willbsp.habits.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willbsp.habits.data.repo.HabitRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HabitViewModel(habitsRepository: HabitRepository) : ViewModel() {

    val habitUiState: StateFlow<HabitUiState> =
        habitsRepository.getAllHabitsStream().map { HabitUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HabitUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}