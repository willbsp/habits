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
import kotlin.properties.Delegates

@HiltViewModel
class LogbookViewModel @Inject constructor(
    private val habitRepository: HabitWithEntriesRepository,
    private val entryRepository: EntryRepository
) : ViewModel() {

    private var selectedHabitId by Delegates.notNull<Int>()

    private val _uiState: MutableStateFlow<LogbookUiState> =
        MutableStateFlow(LogbookUiState.NoHabits)
    val uiState: StateFlow<LogbookUiState> = _uiState

    init {
        viewModelScope.launch {
            val habitWithEntries =
                habitRepository.getHabitsWithEntries().firstOrNull()?.firstOrNull()
            if (habitWithEntries != null) setSelectedHabit(habitWithEntries.habit.id)
        }
    }

    fun setSelectedHabit(habitId: Int) {
        this.selectedHabitId = habitId
        viewModelScope.launch {
            habitRepository.getHabitsWithEntries().collect { list ->
                if (list.isEmpty()) _uiState.value = LogbookUiState.NoHabits
                else _uiState.value = list.toLogbookUiState()
            }
        }
    }

    fun toggleEntry(date: LocalDate) {
        viewModelScope.launch {
            entryRepository.toggleEntry(selectedHabitId, date)
        }
    }

    private fun List<HabitWithEntries>.toLogbookUiState(): LogbookUiState.SelectedHabit {
        val habits = map { LogbookUiState.Habit(it.habit.id, it.habit.name) }
        val entries = find { it.habit.id == selectedHabitId }?.entries?.map { it.date } ?: listOf()
        return LogbookUiState.SelectedHabit(selectedHabitId, entries, habits)
    }


}