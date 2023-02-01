package com.willbsp.habits.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willbsp.habits.data.repo.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class SettingsUiState(
    val showStreaksOnHome: Boolean = false
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    settingsRepository: SettingsRepository
) : ViewModel() {

    val settingsUiState: StateFlow<SettingsUiState> = settingsRepository.showStreaksOnHome.map {
        SettingsUiState(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = SettingsUiState()
    )

    companion object {
        const val TIMEOUT_MILLIS = 5_000L
    }

}