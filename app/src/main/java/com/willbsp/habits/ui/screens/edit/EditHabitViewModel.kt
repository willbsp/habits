package com.willbsp.habits.ui.screens.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willbsp.habits.common.HABIT_NAME_MAX_CHARACTER_LIMIT
import com.willbsp.habits.data.repository.HabitRepository
import com.willbsp.habits.domain.SaveHabitUseCase
import com.willbsp.habits.ui.common.ModifyHabitUiState
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

    var uiState by mutableStateOf(ModifyHabitUiState())
        private set

    private var habitId: Int = checkNotNull(savedStateHandle[HABIT_ID_SAVED_STATE_KEY])

    init {
        loadHabit()
    }

    fun updateUiState(newHabitsUiState: ModifyHabitUiState) {
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
        return if (saveHabitUseCase(uiState.toHabit(habitId), viewModelScope)) {
            true
        } else {
            uiState = uiState.copy(nameIsInvalid = true)
            false
        }
    }

    private fun loadHabit() {
        viewModelScope.launch {
            val habit = habitsRepository.getHabit(habitId)
            if (habit != null) {
                uiState = ModifyHabitUiState(name = habit.name, frequency = habit.frequency)
            }
        }
    }

    companion object {
        private const val HABIT_ID_SAVED_STATE_KEY = "habitId"
    }

}