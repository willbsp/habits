package com.willbsp.habits.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willbsp.habits.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val settingsUiState: StateFlow<SettingsUiState> = settingsRepository.getSettingsMap().map {
        SettingsUiState(
            it[SettingsRepository.SettingsKey.SHOW_STREAKS_ON_HOME] as Boolean? ?: true,
            it[SettingsRepository.SettingsKey.SHOW_COMPLETED_SUBTITLE] as Boolean? ?: true
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = SettingsUiState()
    )

    fun saveStreaksPreference(value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) { // TODO inject dispatcher into settingsRepository
            settingsRepository.saveStreaksPreference(value)
        }
    }

    fun saveSubtitlePreference(value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.saveSubtitlePreference(value)
        }
    }

    companion object {
        const val TIMEOUT_MILLIS = 5_000L
    }

}