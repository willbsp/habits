package com.willbsp.habits.ui.screens.logbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willbsp.habits.data.model.HabitWithEntries
import com.willbsp.habits.data.repository.EntryRepository
import com.willbsp.habits.data.repository.HabitWithEntriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
    clock: Clock
) : ViewModel() {


    private val _logbookUiState = MutableStateFlow(LogbookUiState())
    val logbookUiState: StateFlow<LogbookUiState> = _logbookUiState

    init {
        setSelectedDate(LocalDate.now(clock))
    }

    fun setSelectedDate(date: LocalDate) {
        viewModelScope.launch {
            habitRepository.getHabitsWithEntries().collect { list ->
                _logbookUiState.value = LogbookUiState(
                    list.map { habitWithEntries -> habitWithEntries.toLogbookHabitUiState(date) }
                )
            }
        }
    }

    fun toggleEntry(habitId: Int, date: LocalDate) {
        viewModelScope.launch {
            entryRepository.toggleEntry(habitId, date)
        }
    }

    private fun HabitWithEntries.toLogbookHabitUiState(date: LocalDate): LogbookHabitUiState =
        LogbookHabitUiState(habit.id, habit.name, entries.any { it.date == date })

}