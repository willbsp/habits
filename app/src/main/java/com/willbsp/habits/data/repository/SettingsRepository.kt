package com.willbsp.habits.data.repository

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    val preferences: Flow<Map<Preferences.Key<*>, Any>>
    suspend fun saveStreaksPreference(showStreaksOnHome: Boolean)
    suspend fun saveSubtitlePreference(showCompletedSubtitle: Boolean)

    object SettingsKey {
        val SHOW_STREAKS_ON_HOME = booleanPreferencesKey("show_streaks_on_home")
        val SHOW_COMPLETED_SUBTITLE = booleanPreferencesKey("show_completed_subtitle")
    }

}