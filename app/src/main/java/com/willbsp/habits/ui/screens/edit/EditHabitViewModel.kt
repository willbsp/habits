package com.willbsp.habits.ui.screens.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.repository.HabitRepository
import com.willbsp.habits.ui.common.ModifyHabitUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class EditHabitViewModel @Inject constructor(
    private val habitsRepository: HabitRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var habitUiState by mutableStateOf(ModifyHabitUiState())
        private set

    private var habitId: Int = checkNotNull(savedStateHandle[HABIT_ID_SAVED_STATE_KEY])

    init {
        runBlocking {
            loadHabit()
        }
    }

    fun updateUiState(newHabitsUiState: ModifyHabitUiState) {
        habitUiState = newHabitsUiState.copy()
    }

    fun deleteHabit() {
        viewModelScope.launch {
            habitsRepository.deleteHabit(habitId)
        }
    }

    private fun loadHabit() {
        viewModelScope.launch {
            val habit = habitsRepository.getHabit(habitId)
            if (habit != null) {
                habitUiState = ModifyHabitUiState(name = habit.name, frequency = habit.frequency)
            }
        }
    }

    fun updateHabit() { // TODO validation needed
        viewModelScope.launch {
            habitsRepository.upsertHabit(
                Habit(
                    id = habitId,
                    name = habitUiState.name,
                    frequency = habitUiState.frequency
                )
            )
        }
    }

    companion object {
        private const val HABIT_ID_SAVED_STATE_KEY = "habitId"
    }

}