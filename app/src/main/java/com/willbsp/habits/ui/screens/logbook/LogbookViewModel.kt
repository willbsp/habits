package com.willbsp.habits.ui.screens.logbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willbsp.habits.data.model.HabitWithEntries
import com.willbsp.habits.data.repository.EntryRepository
import com.willbsp.habits.data.repository.HabitWithEntriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class LogbookViewModel @Inject constructor(
    private val habitRepository: HabitWithEntriesRepository,
    private val entryRepository: EntryRepository
) : ViewModel() {

    private var selectedHabitId: Int? = null

    private val _uiState: MutableStateFlow<LogbookUiState> =
        MutableStateFlow(LogbookUiState.NoSelection)
    val uiState: StateFlow<LogbookUiState> = _uiState

    init {
        viewModelScope.launch {
            val habitWithEntries =
                habitRepository.getHabitsWithEntries().firstOrNull()?.firstOrNull()
            if (habitWithEntries != null)
                setSelectedHabit(habitWithEntries.habit.id)
        }
    }

    fun setSelectedHabit(habitId: Int) {
        this.selectedHabitId = habitId
        viewModelScope.launch {
            habitRepository.getHabitsWithEntries().collect { list ->
                if (list.isEmpty()) _uiState.value = LogbookUiState.NoSelection
                else _uiState.value = list.toLogbookUiState()
            }
        }
    }

    private fun List<HabitWithEntries>.toLogbookUiState(): LogbookUiState.SelectedHabit {
        val habits = map { LogbookUiState.Habit(it.habit.id, it.habit.name) }
        val entries = find { it.habit.id == selectedHabitId }?.entries?.map { it.date } ?: listOf()
        return LogbookUiState.SelectedHabit(habits, selectedHabitId!!, entries) // TODO !!
    }

    fun toggleEntry(date: LocalDate) {
        if (selectedHabitId != null) {
            viewModelScope.launch {
                entryRepository.toggleEntry(selectedHabitId!!, date)
            }
        }
    }

}