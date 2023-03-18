package com.willbsp.habits.ui.screens.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willbsp.habits.common.HABIT_NAME_MAX_CHARACTER_LIMIT
import com.willbsp.habits.common.HABIT_NAME_MIN_CHARACTER_LIMIT
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.repository.HabitRepository
import com.willbsp.habits.ui.common.ModifyHabitUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddHabitViewModel @Inject constructor(
    private val habitsRepository: HabitRepository
) : ViewModel() {

    var uiState by mutableStateOf(ModifyHabitUiState())
        private set

    fun updateUiState(newHabitsUiState: ModifyHabitUiState) {
        uiState = if (newHabitsUiState.name.length <= HABIT_NAME_MAX_CHARACTER_LIMIT) {
            newHabitsUiState.copy(nameIsInvalid = false)
        } else newHabitsUiState.copy(nameIsInvalid = true)
    }

    // TODO abstract out save habit validation logic to domain layer?

    fun saveHabit(): Boolean {
        return if (uiState.name.length in (HABIT_NAME_MIN_CHARACTER_LIMIT + 1)..HABIT_NAME_MAX_CHARACTER_LIMIT) {
            viewModelScope.launch {
                habitsRepository.upsertHabit(
                    Habit(name = uiState.name, frequency = uiState.frequency)
                )
            }
            true
        } else {
            uiState = uiState.copy(nameIsInvalid = true)
            false
        }
    }

}