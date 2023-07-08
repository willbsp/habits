package com.willbsp.habits.data.repository.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.willbsp.habits.data.repository.SettingsRepository
import com.willbsp.habits.data.repository.SettingsRepository.SettingsKey.SHOW_COMPLETED_SUBTITLE
import com.willbsp.habits.data.repository.SettingsRepository.SettingsKey.SHOW_SCORE_ON_HOME
import com.willbsp.habits.data.repository.SettingsRepository.SettingsKey.SHOW_STAT_ON_HOME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalSettingsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    override fun getSettingsMap() = dataStore.data.map { it.asMap() }

    override fun getStatisticPreference(): Flow<Boolean> =
        dataStore.data.map { it[SHOW_STAT_ON_HOME] ?: true }

    override fun getSubtitlePreference(): Flow<Boolean> =
        dataStore.data.map { it[SHOW_COMPLETED_SUBTITLE] ?: true }

    override fun getScorePreference(): Flow<Boolean> =
        dataStore.data.map { it[SHOW_SCORE_ON_HOME] ?: false }

    override suspend fun saveStatisticPreference(showStatisticOnHome: Boolean) {
        dataStore.edit { it[SHOW_STAT_ON_HOME] = showStatisticOnHome }
    }

    override suspend fun saveScorePreference(showScoreOnHome: Boolean) {
        dataStore.edit { it[SHOW_SCORE_ON_HOME] = showScoreOnHome }
    }

    override suspend fun saveSubtitlePreference(showCompletedSubtitle: Boolean) {
        dataStore.edit { it[SHOW_COMPLETED_SUBTITLE] = showCompletedSubtitle }
    }

}