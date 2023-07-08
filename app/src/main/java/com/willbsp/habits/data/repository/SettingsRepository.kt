package com.willbsp.habits.data.repository

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun getSettingsMap(): Flow<Map<Preferences.Key<*>, Any>>
    fun getStatisticPreference(): Flow<Boolean>
    fun getSubtitlePreference(): Flow<Boolean>
    fun getScorePreference(): Flow<Boolean>
    suspend fun saveStatisticPreference(showStatisticOnHome: Boolean)
    suspend fun saveScorePreference(showScoreOnHome: Boolean)
    suspend fun saveSubtitlePreference(showCompletedSubtitle: Boolean)

    object SettingsKey {
        val SHOW_STAT_ON_HOME = booleanPreferencesKey("show_stat_on_home")
        val SHOW_SCORE_ON_HOME = booleanPreferencesKey("show_score_on_home")
        val SHOW_COMPLETED_SUBTITLE = booleanPreferencesKey("show_completed_subtitle")
    }

}