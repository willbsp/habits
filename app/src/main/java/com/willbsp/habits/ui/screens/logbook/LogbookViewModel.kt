package com.willbsp.habits.ui.screens.logbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willbsp.habits.data.model.HabitWithEntries
import com.willbsp.habits.data.repo.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class LogbookViewModel @Inject constructor(
    private val habitRepository: HabitRepository,
    private val clock: Clock
) : ViewModel() {


    private val _logbookUiState = MutableStateFlow(LogbookUiState())
    val logbookUiState: StateFlow<LogbookUiState> = _logbookUiState

    init {
        setSelectedDate(LocalDate.now(clock))
    }

    fun setSelectedDate(date: LocalDate) {
        viewModelScope.launch {
            habitRepository.getAllHabitsWithEntriesForDates(listOf(date)).collect {
                _logbookUiState.value = LogbookUiState(
                    it.map { habitWithEntries -> habitWithEntries.toLogbookHabitUiState(date) }
                )
            }
        }
    }

    private fun HabitWithEntries.toLogbookHabitUiState(date: LocalDate): LogbookHabitUiState {
        val habit = this.habit
        val completed = this.entries.any { it.date == date }
        return LogbookHabitUiState(habit.id, habit.name, completed)
    }

    suspend fun toggleEntry(habitId: Int, date: LocalDate) {
        habitRepository.toggleEntry(habitId, date)
    }

}