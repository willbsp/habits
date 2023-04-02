package com.willbsp.habits.ui.screens.logbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willbsp.habits.data.repository.EntryRepository
import com.willbsp.habits.data.repository.HabitRepository
import com.willbsp.habits.domain.model.HabitWithVirtualEntries
import com.willbsp.habits.domain.usecase.GetHabitsWithVirtualEntriesUseCase
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
    private val habitRepository: HabitRepository,
    private val getVirtualEntries: GetHabitsWithVirtualEntriesUseCase,
    private val entryRepository: EntryRepository
) : ViewModel() {

    private var selectedHabitId by Delegates.notNull<Int>()

    private val _uiState: MutableStateFlow<LogbookUiState> =
        MutableStateFlow(LogbookUiState.NoHabits)
    val uiState: StateFlow<LogbookUiState> = _uiState

    init {
        viewModelScope.launch {
            val habit = habitRepository.getAllHabitsStream().firstOrNull()?.firstOrNull()
            if (habit != null) setSelectedHabit(habit.id)
        }
    }

    fun setSelectedHabit(habitId: Int) {
        this.selectedHabitId = habitId
        viewModelScope.launch {
            getVirtualEntries().collect { list ->
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

    private fun List<HabitWithVirtualEntries>.toLogbookUiState(): LogbookUiState.SelectedHabit {

        val habits = this.map { it.habit }
        val entries = this.find { it.habit.id == selectedHabitId }?.entries ?: listOf()

        val completed = entries.filter { it.id != null }.map { it.date }
        val completedByWeek = entries.filter { it.id == null }.map { it.date }

        return LogbookUiState.SelectedHabit(
            habitId = selectedHabitId,
            completed = completed,
            completedByWeek = completedByWeek,
            habits = habits.map { LogbookUiState.Habit(it.id, it.name) }
        )

    }


}