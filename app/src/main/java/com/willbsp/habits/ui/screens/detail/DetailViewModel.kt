package com.willbsp.habits.ui.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willbsp.habits.data.model.HabitFrequency
import com.willbsp.habits.data.repository.HabitRepository
import com.willbsp.habits.data.repository.ReminderRepository
import com.willbsp.habits.domain.usecase.CalculateScoreUseCase
import com.willbsp.habits.domain.usecase.CalculateStatisticsUseCase
import com.willbsp.habits.domain.usecase.CalculateStreakUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.Clock
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    habitRepository: HabitRepository,
    reminderRepository: ReminderRepository,
    savedStateHandle: SavedStateHandle,
    calculateScoreUseCase: CalculateScoreUseCase,
    calculateStreakUseCase: CalculateStreakUseCase,
    calculateStatisticsUseCase: CalculateStatisticsUseCase,
    clock: Clock
) : ViewModel() {

    private var habitId: Int = checkNotNull(savedStateHandle[HABIT_ID_SAVED_STATE_KEY])

    val uiState: StateFlow<DetailUiState> =
        combine(
            calculateScoreUseCase(habitId),
            calculateStreakUseCase(habitId),
            calculateStatisticsUseCase(habitId),
            habitRepository.getHabitStream(habitId),
            reminderRepository.getRemindersForHabitStream(habitId)
        ) { score, streaks, stats, habit, reminders ->

            val habitName = habit?.name ?: ""
            val currentStreak = streaks.find { streak ->
                streak.endDate == LocalDate.now(clock) || streak.endDate == LocalDate.now(clock)
                    .minusDays(1)
            }?.length
            val longestStreak = streaks.maxOfOrNull { it.length }

            DetailUiState(
                habitId = habitId,
                habitName = habitName,
                streak = currentStreak ?: 0,
                longestStreak = longestStreak ?: 0,
                started = stats.started,
                total = stats.total,
                type = habit?.frequency ?: HabitFrequency.DAILY,
                repeat = habit?.repeat ?: 0,
                score = (score?.times(100))?.toInt() ?: 0,
                reminderDays = reminders.map { reminder -> reminder.day }
            )

        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = DetailUiState(habitId)
        )

    companion object {
        const val TIMEOUT_MILLIS = 5_000L
        private const val HABIT_ID_SAVED_STATE_KEY = "habitId"
    }

}