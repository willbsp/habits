package com.willbsp.habits.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willbsp.habits.data.model.Entry
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.repo.HabitRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class HomeUiState(
    val habitsList: List<HabitHomeUiState> = listOf()
)

data class HabitHomeUiState( // TODO better organisation of ui state type names
    val habit: Habit,
    val completed: Boolean
)

class HomeScreenViewModel(private val habitsRepository: HabitRepository) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class) // comment so this actually makes sense TODO
    val homeUiState: StateFlow<HomeUiState> =
        habitsRepository.getAllHabitsStream().flatMapLatest { habitList ->
            val habitHomeUiStateFlows: List<Flow<HabitHomeUiState>> = habitList.map { habit ->
                habitsRepository.entryExistsForDate(getCurrentDate(), habit.id).map {
                    HabitHomeUiState(habit, it)
                }
            }
            combine(habitHomeUiStateFlows) { habitHomeUiStateArray ->
                HomeUiState(habitHomeUiStateArray.map { it })
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = HomeUiState()
        )

    /*fun updateUiState(newHomeUiState: HomeUiState) {
        homeUiState = newHomeUiState;
    }*/

    suspend fun saveEntry(habit: Habit) {
        habitsRepository.insertEntry(
            Entry(
                habitId = habit.id,
                date = getCurrentDate()
            )
        )
    }

    private fun getCurrentDate(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return LocalDateTime.now().format(formatter)
    }

    companion object {
        const val TIMEOUT_MILLIS = 5_000L
    }

}