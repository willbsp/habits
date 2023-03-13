package com.willbsp.habits.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willbsp.habits.data.model.HabitWithEntries
import com.willbsp.habits.data.repository.EntryRepository
import com.willbsp.habits.data.repository.HabitWithEntriesRepository
import com.willbsp.habits.data.repository.SettingsRepository
import com.willbsp.habits.domain.CalculateStreakUseCase
import com.willbsp.habits.ui.common.PreferencesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
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

    val uiState: StateFlow<HomeUiState> = habitRepository.getHabitsWithEntries()
        .map { it.toHomeUiState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = HomeUiState.Empty
        )

    val preferencesUiState: StateFlow<PreferencesUiState> =
        settingsRepository.preferences.map { // TODO make available in domain layer?
            PreferencesUiState(
                showStreaks = it[SettingsRepository.SettingsKey.SHOW_STREAKS_ON_HOME] as Boolean?
                    ?: true,
                showCompletedSubtitle = it[SettingsRepository.SettingsKey.SHOW_COMPLETED_SUBTITLE] as Boolean?
                    ?: true
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = PreferencesUiState()
        )

    fun toggleEntry(habitId: Int, date: LocalDate) {
        viewModelScope.launch {
            entryRepository.toggleEntry(habitId, date)
        }
    }

    private suspend fun List<HabitWithEntries>.toHomeUiState(): HomeUiState {
        return if (this.isEmpty()) HomeUiState.Empty
        else HomeUiState.Habits(this.map { it.toHomeHabitUiState() })
    }

    private suspend fun HabitWithEntries.toHomeHabitUiState(): HomeHabitUiState {
        val streak = calculateStreak(habit.id).first()
        val dates = entries.map { it.date }.sortedDescending()
        return HomeHabitUiState(habit.id, habit.name, streak, dates)
    }

    companion object {
        const val TIMEOUT_MILLIS = 5_000L
    }

}