package com.willbsp.habits.ui.screens.logbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willbsp.habits.data.repository.EntryRepository
import com.willbsp.habits.data.repository.HabitRepository
import com.willbsp.habits.domain.model.HabitWithVirtualEntries
import com.willbsp.habits.domain.usecase.GetHabitsWithVirtualEntriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class LogbookViewModel @Inject constructor(
    private val habitRepository: HabitRepository,
    private val entryRepository: EntryRepository,
    private val clock: Clock,
    private val getVirtualEntries: GetHabitsWithVirtualEntriesUseCase
) : ViewModel() {

    private var selectedHabitId: MutableStateFlow<Int> = MutableStateFlow(-1)

    val uiState: StateFlow<LogbookUiState> = getVirtualEntries()
        .combine(selectedHabitId) { list, _ ->
            if (list.isEmpty()) LogbookUiState.NoHabits
            else list.toLogbookUiState()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = LogbookUiState.NoHabits
        )

    init {
        viewModelScope.launch {
            val habit = habitRepository.getAllHabitsStream().firstOrNull()?.firstOrNull()
            if (habit != null) setSelectedHabit(habit.id)
        }
    }

    fun setSelectedHabit(habitId: Int) {
        this.selectedHabitId.value = habitId
    }

    fun toggleEntry(date: LocalDate) {
        viewModelScope.launch {
            entryRepository.toggleEntry(selectedHabitId.value, date)
        }
    }

    private fun List<HabitWithVirtualEntries>.toLogbookUiState(): LogbookUiState.SelectedHabit {

        val habits = this.map { it.habit }
        val entries = this.find { it.habit.id == selectedHabitId.value }?.entries ?: listOf()

        val completed = entries.filter { it.id != null }.map { it.date }
        val completedByWeek = entries.filter { it.id == null }.map { it.date }

        return LogbookUiState.SelectedHabit(
            habitId = selectedHabitId.value,
            todaysDate = LocalDate.now(clock),
            completed = completed,
            completedByWeek = completedByWeek,
            habits = habits.map { LogbookUiState.Habit(it.id, it.name) }
        )

    }

    companion object {
        const val TIMEOUT_MILLIS = 5_000L
    }

}