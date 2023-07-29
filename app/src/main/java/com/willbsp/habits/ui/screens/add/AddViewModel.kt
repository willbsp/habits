package com.willbsp.habits.ui.screens.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willbsp.habits.data.model.Reminder
import com.willbsp.habits.data.repository.HabitRepository
import com.willbsp.habits.data.repository.ReminderRepository
import com.willbsp.habits.domain.usecase.ValidateHabitNameUseCase
import com.willbsp.habits.ui.common.form.HabitFormUiState
import com.willbsp.habits.ui.common.form.HabitReminderType
import com.willbsp.habits.ui.common.form.toHabit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val habitRepository: HabitRepository,
    private val reminderRepository: ReminderRepository,
    private val isValidHabitName: ValidateHabitNameUseCase
) : ViewModel() {

    var uiState by mutableStateOf(HabitFormUiState.HabitData())
        private set

    fun updateUiState(newUiState: HabitFormUiState.HabitData) {
        uiState = if (isHabitValid()) {
            newUiState.copy(nameIsInvalid = false, daysIsInvalid = false)
        } else newUiState
    }

    fun saveHabit(): Boolean {
        if (isHabitValid()) {
            viewModelScope.launch {
                val habitId = habitRepository.insertHabit(uiState.toHabit()).toInt()
                saveReminders(habitId)
            }
            return true
        }
        return false
    }

    private fun isHabitValid(): Boolean {
        val isNameValid = isValidHabitName(uiState.name)
        val isDaysEmpty =
            uiState.reminderType == HabitReminderType.SPECIFIC && uiState.reminderDays.isEmpty()
        return if (isNameValid && !isDaysEmpty) true
        else {
            uiState = uiState.copy(nameIsInvalid = !isNameValid, daysIsInvalid = isDaysEmpty)
            false
        }
    }

    private suspend fun saveReminders(habitId: Int) {
        when (uiState.reminderType) {
            HabitReminderType.NONE -> return
            HabitReminderType.EVERYDAY -> {
                for (day in Calendar.SUNDAY..Calendar.SATURDAY) {
                    val reminder =
                        Reminder(habitId = habitId, time = uiState.reminderTime, day = day)
                    reminderRepository.upsertReminder(reminder)
                }
            }

            HabitReminderType.SPECIFIC -> {
                for (day in uiState.reminderDays) {
                    val reminder =
                        Reminder(habitId = habitId, time = uiState.reminderTime, day = day)
                    reminderRepository.upsertReminder(reminder)
                }
            }
        }
    }

}