package com.willbsp.habits.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willbsp.habits.data.model.HabitWithEntries
import com.willbsp.habits.data.repository.EntryRepository
import com.willbsp.habits.data.repository.HabitWithEntriesRepository
import com.willbsp.habits.data.repository.SettingsRepository
import com.willbsp.habits.domain.CalculateStreakUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val entryRepository: EntryRepository,
    private val calculateStreak: CalculateStreakUseCase,
    habitRepository: HabitWithEntriesRepository,
    settingsRepository: SettingsRepository,
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> =
        combine(
            habitRepository.getHabitsWithEntries(),
            settingsRepository.getStreakPreference(),
            settingsRepository.getSubtitlePreference()
        ) { habitWithEntriesList, streakPref, subtitlePref ->
            habitWithEntriesList.toHomeUiState(streakPref, subtitlePref)
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

    private suspend fun List<HabitWithEntries>.toHomeUiState(
        streakPref: Boolean,
        subtitlePref: Boolean
    ): HomeUiState {
        return if (this.isEmpty()) HomeUiState.Empty
        else HomeUiState.Habits(this.map { it.toHabit() }, streakPref, subtitlePref)
    }

    private suspend fun HabitWithEntries.toHabit(): HomeUiState.Habit {
        val streak = calculateStreak(habit.id).first().find { streak ->
            streak.endDate == LocalDate.now() || streak.endDate == LocalDate.now().minusDays(1)
        }
        val dates = entries.map { it.date }.sortedDescending()
        return HomeUiState.Habit(habit.id, habit.name, streak?.length, dates)
    }

    companion object {
        const val TIMEOUT_MILLIS = 5_000L
    }

}