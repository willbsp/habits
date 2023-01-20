package com.willbsp.habits.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willbsp.habits.common.getCurrentFormattedDate
import com.willbsp.habits.data.model.HabitEntry
import com.willbsp.habits.data.repo.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.Clock
import javax.inject.Inject

data class HomeUiState(
    val state: List<HomeHabitUiState> = listOf()
)

data class HomeHabitUiState(
    val id: Int,
    val name: String,
    val completed: Boolean
)

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val habitsRepository: HabitRepository,
    private val clock: Clock
) : ViewModel() {

    val homeUiState: StateFlow<HomeUiState> =
        habitsRepository.getHabitEntriesForDateStream(clock.getCurrentFormattedDate()).map {
            HomeUiState(
                it.map { entry ->
                    entry.toHomeHabitUiState()
                }
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = HomeUiState()
        )

    suspend fun toggleEntry(habitId: Int) {
        habitsRepository.toggleEntry(habitId, clock.getCurrentFormattedDate())
    }

    companion object {
        const val TIMEOUT_MILLIS = 5_000L
    }

}

fun HabitEntry.toHomeHabitUiState(): HomeHabitUiState {
    return HomeHabitUiState(this.habitId, this.habitName, this.completed)
}