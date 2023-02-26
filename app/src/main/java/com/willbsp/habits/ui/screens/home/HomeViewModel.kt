package com.willbsp.habits.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willbsp.habits.common.getPreviousDates
import com.willbsp.habits.data.model.HabitWithEntries
import com.willbsp.habits.data.repository.EntryRepository
import com.willbsp.habits.data.repository.HabitWithEntriesRepository
import com.willbsp.habits.data.repository.SettingsRepository
import com.willbsp.habits.domain.CalculateStreakUseCase
import com.willbsp.habits.ui.common.PreferencesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val habitRepository: HabitWithEntriesRepository,
    private val entryRepository: EntryRepository,
    private val settingsRepository: SettingsRepository,
    private val calculateStreak: CalculateStreakUseCase,
    private val clock: Clock
) : ViewModel() {

    val homeUiState: StateFlow<HomeUiState> = habitRepository.getHabitsWithEntries().map { list ->
        HomeUiState(
            list.map { it.toHomeHabitUiState() },
            list.completedCount(),
            list.allCompleted()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = HomeUiState()
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
        viewModelScope.launch(Dispatchers.IO) {
            entryRepository.toggleEntry(habitId, date)
        }
    }

    private suspend fun HabitWithEntries.toHomeHabitUiState(): HomeHabitUiState {

        val dates = clock.getPreviousDates(HABIT_CARD_NUMBER_OF_DAYS)
        val completedDates = dates.map { date ->
            HomeCompletedUiState(date, entries.any { entry -> entry.date == date })
        }
        val streak = calculateStreak(habit.id)

        return HomeHabitUiState(
            habit.id,
            habit.name,
            streak, // habit.calculate streak could fetch entries?
            completedDates
        )

    }

    private fun List<HabitWithEntries>.completedCount(): Int {
        var count = 0
        this.forEach { habitWithEntries ->
            if (habitWithEntries.entries.any { it.date == LocalDate.now(clock) })
                count++
        }
        return count
    }

    private fun List<HabitWithEntries>.allCompleted(): Boolean {
        this.forEach { habitWithEntries ->
            if (!habitWithEntries.entries.any { it.date == LocalDate.now(clock) })
                return false
        }
        return true
    }

    companion object {
        const val TIMEOUT_MILLIS = 5_000L
        const val HABIT_CARD_NUMBER_OF_DAYS = 6
    }

}