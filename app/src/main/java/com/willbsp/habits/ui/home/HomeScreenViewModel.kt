package com.willbsp.habits.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willbsp.habits.data.model.Entry
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.repo.HabitRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class HomeUiState(val habitsList: List<Habit> = listOf())

class HomeScreenViewModel(private val habitsRepository: HabitRepository) : ViewModel() {

    val homeUiState: StateFlow<HomeUiState> =
        habitsRepository.getAllHabitsStream().map {
            HomeUiState(it)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = HomeUiState()
        )

    /*fun updateUiState(newHomeUiState: HomeUiState) {
        homeUiState = newHomeUiState;
    }*/

    suspend fun saveEntry(habit: Habit) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val currentDate = LocalDateTime.now().format(formatter)
        habitsRepository.insertEntry(
            Entry(
                habitId = habit.id,
                date = currentDate,
                completed = true
            )
        )
    }

    companion object {
        const val TIMEOUT_MILLIS = 5_000L
    }

}