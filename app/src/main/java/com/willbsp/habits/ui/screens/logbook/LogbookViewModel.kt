package com.willbsp.habits.ui.screens.logbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willbsp.habits.data.model.HabitWithEntries
import com.willbsp.habits.data.repo.EntryRepository
import com.willbsp.habits.data.repo.HabitWithEntriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class LogbookViewModel @Inject constructor(
    private val habitRepository: HabitWithEntriesRepository,
    private val entryRepository: EntryRepository,
    private val clock: Clock
) : ViewModel() {


    private val _logbookUiState = MutableStateFlow(LogbookUiState())
    val logbookUiState: StateFlow<LogbookUiState> = _logbookUiState

    init {
        setSelectedDate(LocalDate.now(clock))
    }

    fun setSelectedDate(date: LocalDate) {
        viewModelScope.launch {
            habitRepository.getHabitsWithEntries(date).collect { list ->
                _logbookUiState.value = LogbookUiState(
                    list.map { habitWithEntries -> habitWithEntries.toLogbookHabitUiState(date) }
                )
            }
        }
    }

    private fun HabitWithEntries.toLogbookHabitUiState(date: LocalDate): LogbookHabitUiState {
        val habit = this.habit
        val completed = this.entries.any { it.date == date }
        return LogbookHabitUiState(habit.id, habit.name, completed)
    }

    fun toggleEntry(habitId: Int, date: LocalDate) {
        viewModelScope.launch(Dispatchers.IO) {
            entryRepository.toggleEntry(habitId, date)
        }
    }

}