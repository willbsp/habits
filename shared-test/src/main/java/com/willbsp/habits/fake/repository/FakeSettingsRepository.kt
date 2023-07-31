package com.willbsp.habits.fake.repository

import androidx.datastore.preferences.core.Preferences
import com.willbsp.habits.data.repository.SettingsRepository
import com.willbsp.habits.data.repository.SettingsRepository.SettingsKey.SHOW_COMPLETED_SUBTITLE
import com.willbsp.habits.data.repository.SettingsRepository.SettingsKey.SHOW_SCORE_ON_HOME
import com.willbsp.habits.data.repository.SettingsRepository.SettingsKey.SHOW_STAT_ON_HOME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class FakeSettingsRepository : SettingsRepository {

    val preferences = mutableMapOf<Preferences.Key<*>, Any>()
    private val observablePreferences =
        MutableSharedFlow<Map<Preferences.Key<*>, Any>>(1)

    init {
        runBlocking {
            emit()
        }
    }

    suspend fun emit() {
        observablePreferences.emit(preferences)
    }

    override fun getSettingsMap(): Flow<Map<Preferences.Key<*>, Any>> = observablePreferences

    override fun getStatisticPreference(): Flow<Boolean> =
        observablePreferences.map { it[SHOW_STAT_ON_HOME] as Boolean? ?: true }


    override fun getSubtitlePreference(): Flow<Boolean> =
        observablePreferences.map { it[SHOW_COMPLETED_SUBTITLE] as Boolean? ?: true }

    override fun getScorePreference(): Flow<Boolean> =
        observablePreferences.map { it[SHOW_SCORE_ON_HOME] as Boolean? ?: false }

    override suspend fun saveStatisticPreference(showStatisticOnHome: Boolean) {
        preferences[SHOW_STAT_ON_HOME] = showStatisticOnHome
        emit()
    }

    override suspend fun saveScorePreference(showScoreOnHome: Boolean) {
        preferences[SHOW_SCORE_ON_HOME] = showScoreOnHome
        emit()
    }

    override suspend fun saveSubtitlePreference(showCompletedSubtitle: Boolean) {
        preferences[SHOW_COMPLETED_SUBTITLE] = showCompletedSubtitle
        emit()
    }

}