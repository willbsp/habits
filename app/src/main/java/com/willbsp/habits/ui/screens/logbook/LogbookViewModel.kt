package com.willbsp.habits.ui.screens.logbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willbsp.habits.common.getCurrentFormattedDate
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.repo.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.Clock
import javax.inject.Inject

data class LogbookUiState(
    val habits: List<LogbookHabitUiState> = listOf()
)

data class LogbookHabitUiState(
    val habit: Habit,
    val completed: Boolean
)

@HiltViewModel
class LogbookViewModel @Inject constructor(
    private val habitRepository: HabitRepository,
    private val clock: Clock
) : ViewModel() {

    private var selectedDate = clock.getCurrentFormattedDate()

    val logbookUiState: StateFlow<LogbookUiState> =
        habitRepository.getHabitsCompletedForDateStream(selectedDate)
            .map {
                LogbookUiState(
                    it.map { habitWithCompleted ->
                        LogbookHabitUiState(habitWithCompleted.first, habitWithCompleted.second)
                    }
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = LogbookUiState()
            )

    fun setSelectedDate(newDate: String) {
        selectedDate = newDate
    }

    companion object {
        const val TIMEOUT_MILLIS = 5_000L
    }

}