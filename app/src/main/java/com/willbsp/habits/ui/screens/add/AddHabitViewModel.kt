package com.willbsp.habits.ui.screens.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willbsp.habits.common.HABIT_NAME_MAX_CHARACTER_LIMIT
import com.willbsp.habits.domain.usecase.SaveHabitUseCase
import com.willbsp.habits.ui.common.HabitUiState
import com.willbsp.habits.ui.common.toHabit
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddHabitViewModel @Inject constructor(
    private val saveHabitUseCase: SaveHabitUseCase
) : ViewModel() {

    var uiState by mutableStateOf(HabitUiState.Habit())
        private set

    fun updateUiState(newHabitsUiState: HabitUiState.Habit) {
        uiState = if (newHabitsUiState.name.length <= HABIT_NAME_MAX_CHARACTER_LIMIT) {
            newHabitsUiState.copy(nameIsInvalid = false)
        } else newHabitsUiState.copy(nameIsInvalid = true)
    }

    fun saveHabit(): Boolean {
        return if (saveHabitUseCase(uiState.toHabit(), viewModelScope)) {
            true
        } else {
            uiState = uiState.copy(nameIsInvalid = true)
            false
        }
    }

}