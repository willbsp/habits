package com.willbsp.habits.ui.screens.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willbsp.habits.common.HABIT_NAME_MAX_CHARACTER_LIMIT
import com.willbsp.habits.data.repository.HabitRepository
import com.willbsp.habits.domain.usecase.SaveHabitUseCase
import com.willbsp.habits.ui.common.HabitUiState
import com.willbsp.habits.ui.common.toHabit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditHabitViewModel @Inject constructor(
    private val habitsRepository: HabitRepository,
    private val saveHabitUseCase: SaveHabitUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var uiState: HabitUiState by mutableStateOf(HabitUiState.Loading)
        private set

    private var habitId: Int = checkNotNull(savedStateHandle[HABIT_ID_SAVED_STATE_KEY])

    init {
        loadHabit()
    }

    fun updateUiState(newHabitsUiState: HabitUiState.Habit) {
        uiState = if (newHabitsUiState.name.length <= HABIT_NAME_MAX_CHARACTER_LIMIT) {
            newHabitsUiState.copy(nameIsInvalid = false)
        } else newHabitsUiState.copy(nameIsInvalid = true)
    }

    fun deleteHabit() {
        viewModelScope.launch {
            habitsRepository.deleteHabit(habitId)
        }
    }

    fun saveHabit(): Boolean {
        return when (uiState) {
            is HabitUiState.Habit -> {
                val habitState = uiState as HabitUiState.Habit
                return if (saveHabitUseCase(habitState.toHabit(habitId), viewModelScope)) {
                    true
                } else {
                    uiState = habitState.copy(nameIsInvalid = true)
                    false
                }
            }

            else -> false
        }
    }

    private fun loadHabit() {
        viewModelScope.launch {
            val habit = habitsRepository.getHabit(habitId)
            if (habit != null) {
                uiState = HabitUiState.Habit(
                    name = habit.name,
                    frequency = habit.frequency,
                    repeat = habit.repeat
                )
            }
        }
    }

    companion object {
        private const val HABIT_ID_SAVED_STATE_KEY = "habitId"
    }

}