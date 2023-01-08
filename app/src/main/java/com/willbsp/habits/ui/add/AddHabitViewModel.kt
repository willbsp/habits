package com.willbsp.habits.ui.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.willbsp.habits.data.Habit
import com.willbsp.habits.data.HabitFrequency
import com.willbsp.habits.data.repo.HabitRepository

data class HabitUiState(
    val name: String = "",
    val frequency: HabitFrequency = HabitFrequency.DAILY
)

class AddHabitViewModel(private val habitsRepository: HabitRepository) : ViewModel() {

    var habitUiState by mutableStateOf(HabitUiState())
        private set

    fun updateUiState(newHabitsUiState: HabitUiState) {
        habitUiState = newHabitsUiState.copy()
    }

    suspend fun saveHabit() { // TODO validation needed
        habitsRepository.insertHabit(Habit(name = habitUiState.name, frequency = habitUiState.frequency))
    }

}