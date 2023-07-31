package com.willbsp.habits.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willbsp.habits.data.repository.EntryRepository
import com.willbsp.habits.data.repository.SettingsRepository
import com.willbsp.habits.domain.model.HabitWithVirtualEntries
import com.willbsp.habits.domain.usecase.CalculateScoreUseCase
import com.willbsp.habits.domain.usecase.CalculateStreakUseCase
import com.willbsp.habits.domain.usecase.GetHabitsWithVirtualEntriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val entryRepository: EntryRepository,
    private val calculateStreak: CalculateStreakUseCase,
    private val calculateScore: CalculateScoreUseCase,
    private val clock: Clock,
    getHabitsWithVirtualEntries: GetHabitsWithVirtualEntriesUseCase,
    settingsRepository: SettingsRepository,
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> =
        combine(
            getHabitsWithVirtualEntries(),
            settingsRepository.getStatisticPreference(),
            settingsRepository.getSubtitlePreference(),
            settingsRepository.getScorePreference()
        ) { habitWithEntriesList, streakPref, subtitlePref, scorePref ->
            habitWithEntriesList.toHomeUiState(streakPref, subtitlePref, scorePref)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = HomeUiState.Empty
        )

    fun toggleEntry(habitId: Int, date: LocalDate) {
        viewModelScope.launch {
            entryRepository.toggleEntry(habitId, date)
        }
    }

    private suspend fun List<HabitWithVirtualEntries>.toHomeUiState(
        streakPref: Boolean,
        subtitlePref: Boolean,
        scorePref: Boolean
    ): HomeUiState {
        return if (this.isEmpty()) HomeUiState.Empty
        else HomeUiState.Habits(
            this.map { it.toHabit() },
            LocalDate.now(clock),
            streakPref,
            scorePref,
            subtitlePref,
        )
    }

    private suspend fun HabitWithVirtualEntries.toHabit(): HomeUiState.Habit {
        val streak = calculateStreak(habit.id).first().find { streak ->
            streak.endDate == LocalDate.now(clock)
                    || streak.endDate == LocalDate.now(clock).minusDays(1)
        }
        var score = calculateScore(habit.id).first()?.times(100)?.toInt()
        if (score == 0) score = null
        val completed = entries.filter { it.id != null }.map { it.date }.sortedDescending()
        val completedByWeek = entries.filter { it.id == null }.map { it.date }.sortedDescending()
        return HomeUiState.Habit(
            habit.id,
            habit.name,
            habit.frequency,
            streak?.length,
            score,
            completed,
            completedByWeek
        )
    }

    companion object {
        const val TIMEOUT_MILLIS = 5_000L
    }

}