package com.willbsp.habits.ui.screens.logbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willbsp.habits.data.repository.EntryRepository
import com.willbsp.habits.data.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class LogbookViewModel @Inject constructor(
    private val habitRepository: HabitRepository,
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
        selectedHabitId = habitId
        viewModelScope.launch {

            combine(
                habitRepository.getAllHabitsStream(),
                entryRepository.getAllEntriesStream(habitId)
            ) { habitList, entryList ->
                if (habitList.isEmpty()) LogbookUiState.NoHabits
                else LogbookUiState.SelectedHabit(
                    selectedHabitId,
                    entryList.map { it.date },
                    habitList.map { LogbookUiState.Habit(it.id, it.name) }
                )
            }.collect { logbookUiState ->
                _uiState.value = logbookUiState
            }

        }
    }

    fun toggleEntry(date: LocalDate) {
        viewModelScope.launch {
            entryRepository.toggleEntry(selectedHabitId, date)
        }
    }

}