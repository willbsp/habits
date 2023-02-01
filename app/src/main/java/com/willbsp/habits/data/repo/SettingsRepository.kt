package com.willbsp.habits.data.repo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    val showStreaksOnHome: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[SHOW_STREAKS_ON_HOME] ?: true
    }

    suspend fun saveStreaksPreference(showStreaksOnHome: Boolean) {
        dataStore.edit { preferences ->
            preferences[SHOW_STREAKS_ON_HOME] = showStreaksOnHome
        }
    }

    private companion object {
        val SHOW_STREAKS_ON_HOME = booleanPreferencesKey("show_streaks_on_home")
    }

}