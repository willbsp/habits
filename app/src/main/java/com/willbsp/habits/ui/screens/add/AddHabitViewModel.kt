package com.willbsp.habits.ui.screens.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.repo.HabitRepository
import com.willbsp.habits.ui.common.ModifyHabitUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddHabitViewModel @Inject constructor(
    private val habitsRepository: HabitRepository
) : ViewModel() {

    var habitUiState by mutableStateOf(ModifyHabitUiState())
        private set

    fun updateUiState(newHabitsUiState: ModifyHabitUiState) {
        // TODO check for valid here
        habitUiState = newHabitsUiState.copy()
    }

    suspend fun saveHabit() { // TODO validation needed
        habitsRepository.addHabit(
            Habit(
                name = habitUiState.name,
                frequency = habitUiState.frequency
            )
        )
    }

}