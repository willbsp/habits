package com.willbsp.habits.ui.screens.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willbsp.habits.data.repository.HabitRepository
import com.willbsp.habits.data.repository.ReminderRepository
import com.willbsp.habits.domain.model.HabitReminderType
import com.willbsp.habits.domain.usecase.SaveHabitUseCase
import com.willbsp.habits.ui.common.form.HabitFormUiState
import com.willbsp.habits.ui.common.form.toHabitData
import com.willbsp.habits.util.ReminderManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    private val habitRepository: HabitRepository,
    private val reminderManager: ReminderManager,
    private val reminderRepository: ReminderRepository,
    private val saveHabit: SaveHabitUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var uiState: HabitFormUiState by mutableStateOf(HabitFormUiState.Loading)
        private set

    private var habitId: Int = checkNotNull(savedStateHandle[HABIT_ID_SAVED_STATE_KEY])

    init {
        loadHabit()
    }

    fun updateUiState(newUiState: HabitFormUiState.Data) {
        uiState = newUiState
    }

    fun saveHabit(): Boolean {
        if (uiState is HabitFormUiState.Data && isHabitValid()) {
            viewModelScope.launch {
                saveHabit((uiState as HabitFormUiState.Data).toHabitData(), habitId)
            }
            return true
        }
        return false
    }

    private fun isHabitValid(): Boolean {
        if (uiState is HabitFormUiState.Data) {
            val data = uiState as HabitFormUiState.Data
            val isNameValid = data.isNameValid()
            val isDaysValid = data.isDaysValid()
            return if (isNameValid && isDaysValid) true
            else {
                uiState = data.copy(nameIsInvalid = !isNameValid, daysIsInvalid = !isDaysValid)
                false
            }
        }
        return false
    }

    fun deleteHabit() {
        viewModelScope.launch {
            reminderManager.unscheduleAllReminders(habitId)
            habitRepository.deleteHabit(habitId)
        }
    }

    private fun loadHabit() {
        viewModelScope.launch {
            val habit = habitRepository.getHabit(habitId)
            if (habit != null) {
                val reminders = reminderRepository.getRemindersForHabitStream(habitId).first()
                uiState = HabitFormUiState.Data(
                    name = habit.name,
                    frequency = habit.frequency,
                    repeat = habit.repeat,
                    reminderType = getReminderType(reminders.count()),
                    reminderTime = if (reminders.isNotEmpty()) reminders.first().time else LocalTime.NOON,
                    reminderDays = reminders.map { it.day }.toSet()
                )
            }
        }
    }

    private fun getReminderType(reminderCount: Int): HabitReminderType {
        return when (reminderCount) {
            7 -> HabitReminderType.EVERYDAY
            0 -> HabitReminderType.NONE
            else -> HabitReminderType.SPECIFIC
        }
    }

    companion object {
        private const val HABIT_ID_SAVED_STATE_KEY = "habitId"
    }

}