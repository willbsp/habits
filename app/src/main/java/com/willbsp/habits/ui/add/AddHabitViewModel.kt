package com.willbsp.habits.ui.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.model.HabitFrequency
import com.willbsp.habits.data.repo.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class AddHabitUiState(
    val name: String = "",
    val frequency: HabitFrequency = HabitFrequency.DAILY
)

@HiltViewModel
class AddHabitViewModel @Inject constructor(
    private val habitsRepository: HabitRepository
) : ViewModel() {

    var habitUiState by mutableStateOf(AddHabitUiState())
        private set

    fun updateUiState(newHabitsUiState: AddHabitUiState) {
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