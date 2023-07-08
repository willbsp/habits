package com.willbsp.habits.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willbsp.habits.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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

    val uiState: StateFlow<SettingsUiState> = settingsRepository.getSettingsMap().map {
        SettingsUiState(
            showStatistic =
            it[SettingsRepository.SettingsKey.SHOW_STAT_ON_HOME] as Boolean? ?: true,
            showCompletedSubtitle =
            it[SettingsRepository.SettingsKey.SHOW_COMPLETED_SUBTITLE] as Boolean? ?: true,
            showScore =
            it[SettingsRepository.SettingsKey.SHOW_SCORE_ON_HOME] as Boolean? ?: false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = SettingsUiState()
    )

    fun saveStatisticPreference(value: Boolean) {
        viewModelScope.launch {
            settingsRepository.saveStatisticPreference(value)
        }
    }

    fun saveSubtitlePreference(value: Boolean) {
        viewModelScope.launch {
            settingsRepository.saveSubtitlePreference(value)
        }
    }

    fun saveScorePreference(value: Boolean) {
        viewModelScope.launch {
            settingsRepository.saveScorePreference(value)
        }
    }

    companion object {
        const val TIMEOUT_MILLIS = 5_000L
    }

}