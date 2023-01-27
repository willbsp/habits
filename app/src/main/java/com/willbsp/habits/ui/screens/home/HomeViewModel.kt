package com.willbsp.habits.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willbsp.habits.common.getPreviousDaysFormattedDate
import com.willbsp.habits.data.repo.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.Clock
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val habitsRepository: HabitRepository,
    private val clock: Clock
) : ViewModel() {

    val homeUiState: StateFlow<HomeUiState> =
        habitsRepository.getHabitsCompletedForDatesStream(clock.getPreviousDaysFormattedDate(6))
            .map {
                HomeUiState(
                    it.map { habitWithCompletedList ->
                        HomeHabitUiState(
                            habitWithCompletedList.first.id,
                            habitWithCompletedList.first.name,
                            habitWithCompletedList.second.map { completed ->
                                HomeCompletedUiState(completed.first, completed.second)
                            })
                    }
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )

    suspend fun toggleEntry(habitId: Int, date: String) {
        habitsRepository.toggleEntry(habitId, date)
    }

    companion object {
        const val TIMEOUT_MILLIS = 5_000L
    }

}