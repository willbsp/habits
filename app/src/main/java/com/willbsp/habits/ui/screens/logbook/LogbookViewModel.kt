package com.willbsp.habits.ui.screens.logbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
            habitRepository.getHabitsCompletedForDateStream(date).collect {
                _logbookUiState.value = LogbookUiState(
                    it.map { habitWithCompleted ->
                        LogbookHabitUiState(
                            habitWithCompleted.first.id,
                            habitWithCompleted.first.name,
                            habitWithCompleted.second
                        )
                    }
                )
            }
        }

    }

}