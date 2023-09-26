package com.willbsp.habits.ui.screens.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willbsp.habits.domain.usecase.SaveHabitUseCase
import com.willbsp.habits.ui.common.form.HabitFormUiState
import com.willbsp.habits.ui.common.form.toHabitData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val saveHabit: SaveHabitUseCase
) : ViewModel() {

    var uiState by mutableStateOf(HabitFormUiState.Data())
        private set

    fun updateUiState(newUiState: HabitFormUiState.Data) {
        uiState = newUiState
    }

    fun saveHabit(): Boolean {
        if (isHabitValid()) {
            viewModelScope.launch {
                saveHabit(uiState.toHabitData())
            }
            return true
        }
        return false
    }

    private fun isHabitValid(): Boolean {
        val isNameValid = uiState.isNameValid()
        val isDaysValid = uiState.isDaysValid()
        return if (isNameValid && isDaysValid) true
        else {
            uiState = uiState.copy(nameIsInvalid = !isNameValid, daysIsInvalid = !isDaysValid)
            false
        }
    }

}