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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val entryRepository: EntryRepository,
    private val calculateStreak: CalculateStreakUseCase,
    private val clock: Clock,
    habitRepository: HabitWithEntriesRepository,
    settingsRepository: SettingsRepository,
) : ViewModel() {

    val homeUiState: StateFlow<HomeUiState> = habitRepository.getHabitsWithEntries().map { list ->
        val habitState = list.getHabitState()
        HomeUiState(
            list.map { it.toHomeHabitUiState() },
            list.completedCount(),
            habitState
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

        val completedDates = (0..HABIT_CARD_PREVIOUS_DAYS).map {
            val date = LocalDate.now(clock).minusDays(it.toLong())
            HomeCompletedUiState(date, entries.any { entry -> entry.date == date })
        }

        val streak = calculateStreak(habit.id).first()

        return HomeHabitUiState(
            habit.id,
            habit.name,
            streak,
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

    private fun List<HabitWithEntries>.getHabitState(): HabitState {

        if (!this.any()) return HabitState.NO_HABITS

        val allCompleted = this.map { habit ->
            return@map habit.entries.any { it.date == LocalDate.now(clock) }
        }.all { it }

        return if (allCompleted) HabitState.ALL_COMPLETED
        else HabitState.SHOW_HABITS

    }

    companion object {
        const val TIMEOUT_MILLIS = 5_000L
        const val HABIT_CARD_PREVIOUS_DAYS: Int = 5
    }

}