package com.willbsp.habits.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willbsp.habits.common.getPreviousDatesList
import com.willbsp.habits.data.model.HabitWithEntries
import com.willbsp.habits.data.repo.HabitRepository
import com.willbsp.habits.data.repo.SettingsRepository
import com.willbsp.habits.ui.common.PreferencesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.Clock
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val habitsRepository: HabitRepository,
    private val settingsRepository: SettingsRepository,
    private val clock: Clock
) : ViewModel() {

    val homeUiState: StateFlow<HomeUiState> =
        habitsRepository.getAllHabitsWithEntriesForDates(
            clock.getPreviousDatesList(
                HABIT_CARD_NUMBER_OF_DAYS
            )
        ).map { habitWithEntriesList ->
            HomeUiState(
                habitWithEntriesList.map { habitWithEntries ->
                    habitWithEntries.calculateStreak()
                    habitWithEntries.toHomeHabitUiState()
                },
                allCompleted = habitWithEntriesList.allCompleted(),
                completedCount = habitWithEntriesList.completedCount()
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = HomeUiState()
        )

    val preferencesUiState: StateFlow<PreferencesUiState> = settingsRepository.preferences.map {
        PreferencesUiState(
            showStreaks = it[SettingsRepository.SettingsKey.SHOW_STREAKS_ON_HOME] as Boolean,
            showCompletedSubtitle = it[SettingsRepository.SettingsKey.SHOW_COMPLETED_SUBTITLE] as Boolean
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = PreferencesUiState()
    )

    suspend fun toggleEntry(habitId: Int, date: LocalDate) {
        habitsRepository.toggleEntry(habitId, date)
    }

    private fun HabitWithEntries.toHomeHabitUiState(): HomeHabitUiState {

        val habit = this.habit
        val entries = this.entries

        val dates = clock.getPreviousDatesList(HABIT_CARD_NUMBER_OF_DAYS)
        val completedDates = dates.map { date ->
            HomeCompletedUiState(date, entries.any { entry -> entry.date == date })
        }

        return HomeHabitUiState(habit.id, habit.name, this.calculateStreak(), completedDates)

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

    private fun HabitWithEntries.calculateStreak(): Int? {

        val date = LocalDate.now(clock)
        val yesterday = date.minusDays(1)
        val entries = this.entries.sortedByDescending { it.date }

        var streak = 0
        if (entries.isNotEmpty()) entries.forEach { entry ->
            if (entry.date == yesterday.minusDays(streak.toLong())) streak++
            else return@forEach
        } else {
            return null
        }

        if (entries.first().date == date) streak++

        return if (streak > 1) streak
        else null

    }

    companion object {
        const val TIMEOUT_MILLIS = 5_000L
        const val HABIT_CARD_NUMBER_OF_DAYS = 6
    }

}