package com.willbsp.habits.data.repo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.willbsp.habits.data.repo.SettingsRepository.SettingsKey.SHOW_STREAKS_ON_HOME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalSettingsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    override val showStreaksOnHome: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[SHOW_STREAKS_ON_HOME] ?: true
    }

    override suspend fun saveStreaksPreference(showStreaksOnHome: Boolean) {
        dataStore.edit { preferences ->
            preferences[SHOW_STREAKS_ON_HOME] = showStreaksOnHome
        }
    }


}