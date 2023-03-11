package com.willbsp.habits.ui.screens.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.repository.HabitRepository
import com.willbsp.habits.ui.common.ModifyHabitUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddHabitViewModel @Inject constructor(
    private val habitsRepository: HabitRepository
) : ViewModel() {

    var uiState by mutableStateOf(ModifyHabitUiState())
        private set

    fun updateUiState(newHabitsUiState: ModifyHabitUiState) {
        // TODO check for valid here
        uiState = newHabitsUiState.copy()
    }

    suspend fun saveHabit() { // TODO validation needed
        habitsRepository.addHabit(
            Habit(
                name = uiState.name,
                frequency = uiState.frequency
            )
        )
    }

}