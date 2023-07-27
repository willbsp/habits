package com.willbsp.habits.ui.screens.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willbsp.habits.data.repository.HabitRepository
import com.willbsp.habits.domain.usecase.ValidateHabitNameUseCase
import com.willbsp.habits.ui.common.HabitUiState
import com.willbsp.habits.ui.common.toHabit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddHabitViewModel @Inject constructor(
    private val habitRepository: HabitRepository,
    private val isValidHabitName: ValidateHabitNameUseCase
) : ViewModel() {

    var uiState by mutableStateOf(HabitUiState.Habit())
        private set

    fun updateUiState(newUiState: HabitUiState.Habit) {
        uiState = if (isValidHabitName(newUiState.name)) {
            newUiState.copy(nameIsInvalid = false)
        } else newUiState.copy(nameIsInvalid = true)
    }

    fun saveHabit(): Boolean {
        if (isValidHabitName(uiState.name)) {
            viewModelScope.launch { habitRepository.upsertHabit(uiState.toHabit()) }
            return true
        }
        return false
    }

}