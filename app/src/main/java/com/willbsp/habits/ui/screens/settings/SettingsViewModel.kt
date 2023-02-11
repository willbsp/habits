package com.willbsp.habits.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willbsp.habits.data.repo.SettingsRepository
import com.willbsp.habits.ui.common.PreferencesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val preferencesUiState: StateFlow<PreferencesUiState> = settingsRepository.preferences.map {
        PreferencesUiState(
            it[SettingsRepository.SettingsKey.SHOW_STREAKS_ON_HOME] as Boolean? ?: true,
            it[SettingsRepository.SettingsKey.SHOW_COMPLETED_SUBTITLE] as Boolean? ?: true
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = PreferencesUiState()
    )

    suspend fun saveStreaksPreference(value: Boolean) {
        settingsRepository.saveStreaksPreference(value)
    }

    suspend fun saveSubtitlePreference(value: Boolean) {
        settingsRepository.saveSubtitlePreference(value)
    }

    companion object {
        const val TIMEOUT_MILLIS = 5_000L
    }

}