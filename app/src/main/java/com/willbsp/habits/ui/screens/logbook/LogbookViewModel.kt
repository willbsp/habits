package com.willbsp.habits.ui.screens.logbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willbsp.habits.data.repository.EntryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class LogbookViewModel @Inject constructor(
    private val entryRepository: EntryRepository
) : ViewModel() {

    private var habitId: Int? = null

    private val _uiState: MutableStateFlow<LogbookUiState> =
        MutableStateFlow(LogbookUiState.NoSelection)
    val uiState: StateFlow<LogbookUiState> = _uiState

    init {
        setSelectedHabit(1)
    }

    fun setSelectedHabit(habitId: Int) {
        this.habitId = habitId
        viewModelScope.launch {
            entryRepository.getAllEntriesStream(habitId).collect { list ->
                _uiState.value = LogbookUiState.Dates(list.map { it.date })
            }
        }
    }

    fun toggleEntry(date: LocalDate) {
        if (habitId != null) {
            viewModelScope.launch {
                entryRepository.toggleEntry(habitId!!, date)
            }
        }
    }

}