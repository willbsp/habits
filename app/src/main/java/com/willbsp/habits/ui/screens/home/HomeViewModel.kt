package com.willbsp.habits.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willbsp.habits.common.getPreviousDatesList
import com.willbsp.habits.data.model.HabitWithEntries
import com.willbsp.habits.data.repo.HabitRepository
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
    private val clock: Clock
) : ViewModel() {

    val homeUiState: StateFlow<HomeUiState> =
        habitsRepository.getAllHabitsWithEntries(clock.getPreviousDatesList(6))
            .map { habitWithEntriesList ->
                HomeUiState(habitWithEntriesList.map {
                    it.calculateStreak()
                    it.toHomeHabitUiState()
                })
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )

    suspend fun toggleEntry(habitId: Int, date: LocalDate) {
        habitsRepository.toggleEntry(habitId, date)
    }

    private fun HabitWithEntries.toHomeHabitUiState(): HomeHabitUiState {

        val habit = this.habit
        val entries = this.entries

        val dates = clock.getPreviousDatesList(6)
        val completedDates = dates.map { date ->
            HomeCompletedUiState(date, entries.any { entry -> entry.date == date })
        }

        return HomeHabitUiState(habit.id, habit.name, this.calculateStreak(), completedDates)

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
    }

}