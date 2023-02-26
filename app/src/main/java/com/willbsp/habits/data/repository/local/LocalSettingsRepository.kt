package com.willbsp.habits.data.repository.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.willbsp.habits.data.repository.SettingsRepository
import com.willbsp.habits.data.repository.SettingsRepository.SettingsKey.SHOW_COMPLETED_SUBTITLE
import com.willbsp.habits.data.repository.SettingsRepository.SettingsKey.SHOW_STREAKS_ON_HOME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalSettingsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    override val preferences: Flow<Map<Preferences.Key<*>, Any>> = dataStore.data.map { it.asMap() }

    override suspend fun saveStreaksPreference(showStreaksOnHome: Boolean) {
        dataStore.edit { it[SHOW_STREAKS_ON_HOME] = showStreaksOnHome }
    }

    override suspend fun saveSubtitlePreference(showCompletedSubtitle: Boolean) {
        dataStore.edit { it[SHOW_COMPLETED_SUBTITLE] = showCompletedSubtitle }
    }

}