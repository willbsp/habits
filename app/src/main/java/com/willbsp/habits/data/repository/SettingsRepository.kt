package com.willbsp.habits.data.repository

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun getSettingsMap(): Flow<Map<Preferences.Key<*>, Any>>
    fun getStreakPreference(): Flow<Boolean>
    fun getSubtitlePreference(): Flow<Boolean>
    suspend fun saveStreaksPreference(showStreaksOnHome: Boolean)
    suspend fun saveSubtitlePreference(showCompletedSubtitle: Boolean)

    object SettingsKey {
        val SHOW_STREAKS_ON_HOME = booleanPreferencesKey("show_streaks_on_home")
        val SHOW_COMPLETED_SUBTITLE = booleanPreferencesKey("show_completed_subtitle")
    }

}