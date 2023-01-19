package com.willbsp.habits.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willbsp.habits.data.model.Entry
import com.willbsp.habits.data.repo.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.Clock
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
        habitsRepository.getTodaysHabitEntriesStream().map {
            HomeUiState(
                it.map { entry ->
                    HomeHabitUiState(entry.habitId, entry.habitName, entry.completed)
                }
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = HomeUiState()
        )

    suspend fun toggleEntry(habitId: Int) {
        val date: String = getCurrentDate()
        val entry: Entry? = habitsRepository.getEntryForDate(date, habitId)
        if (entry == null) {
            habitsRepository.insertEntry(
                Entry(
                    habitId = habitId,
                    date = date
                )
            )
        } else {
            habitsRepository.deleteEntry(entry)
        }
    }

    private fun getCurrentDate(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") // TODO make constant
        return LocalDateTime.now(clock).format(formatter)
    }

    companion object {
        const val TIMEOUT_MILLIS = 5_000L
    }

}