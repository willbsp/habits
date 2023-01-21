package com.willbsp.habits.ui.screens.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.repo.HabitRepository
import com.willbsp.habits.ui.screens.common.ModifyHabitUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class EditHabitViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val habitsRepository: HabitRepository
) : ViewModel() {

    var habitUiState by mutableStateOf(ModifyHabitUiState())
        private set

    private var habitId: Int = checkNotNull(savedStateHandle["habitId"])

    init {
        runBlocking {
            loadHabit()
        }
    }

    suspend fun loadHabit() {
        val habit = habitsRepository.getHabitById(habitId)
        habitUiState = ModifyHabitUiState(habit.name, habit.frequency)
    }

    fun updateUiState(newHabitsUiState: ModifyHabitUiState) {
        habitUiState = newHabitsUiState.copy()
    }

    suspend fun updateHabit() { // TODO validation needed
        habitsRepository.updateHabit(
            Habit(
                id = habitId,
                name = habitUiState.name,
                frequency = habitUiState.frequency
            )
        )
    }

}