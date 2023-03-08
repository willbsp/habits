package com.willbsp.habits.ui.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willbsp.habits.data.repository.HabitRepository
import com.willbsp.habits.domain.CalculateScoreUseCase
import com.willbsp.habits.domain.CalculateStreakUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    habitRepository: HabitRepository,
    savedStateHandle: SavedStateHandle,
    calculateScoreUseCase: CalculateScoreUseCase,
    calculateStreakUseCase: CalculateStreakUseCase
) : ViewModel() {

    private var habitId: Int = checkNotNull(savedStateHandle[HABIT_ID_SAVED_STATE_KEY])

    val detailUiState: StateFlow<DetailUiState> =
        combine(
            calculateScoreUseCase(habitId),
            calculateStreakUseCase(habitId),
            habitRepository.getHabitStream(habitId)
        ) { score, streak, habit ->
            val habitName = habit?.name ?: ""
            if (streak != null && score != null) {
                DetailUiState(
                    habitId,
                    habitName,
                    streak,
                    (score * 100).toInt()
                )
            } else if (score != null) {
                DetailUiState(
                    habitId,
                    habitName,
                    0,
                    (score * 100).toInt()
                )
            } else {
                DetailUiState(habitId, habitName)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = DetailUiState(habitId)
        )

    companion object {
        const val TIMEOUT_MILLIS = 5_000L
        private const val HABIT_ID_SAVED_STATE_KEY = "habitId"
    }

}