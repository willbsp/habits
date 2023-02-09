package com.willbsp.habits.data.repo

import androidx.datastore.preferences.core.booleanPreferencesKey
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    suspend fun saveStreaksPreference(showStreaksOnHome: Boolean)
    val showStreaksOnHome: Flow<Boolean>

    object SettingsKey {
        val SHOW_STREAKS_ON_HOME = booleanPreferencesKey("show_streaks_on_home")
        val SHOW_COMPLETED_SUBTITLE = booleanPreferencesKey("show_completed_subtitle")
    }

}