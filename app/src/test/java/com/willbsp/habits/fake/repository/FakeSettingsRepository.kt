package com.willbsp.habits.fake.repository

import androidx.datastore.preferences.core.Preferences
import com.willbsp.habits.data.repository.SettingsRepository
import com.willbsp.habits.data.repository.SettingsRepository.SettingsKey.SHOW_COMPLETED_SUBTITLE
import com.willbsp.habits.data.repository.SettingsRepository.SettingsKey.SHOW_STREAKS_ON_HOME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeSettingsRepository : SettingsRepository {

    var prefs = mutableMapOf<Preferences.Key<*>, Any>()

    override val preferences: Flow<Map<Preferences.Key<*>, Any>>
        get() = flow { prefs }

    override suspend fun saveStreaksPreference(showStreaksOnHome: Boolean) {
        prefs[SHOW_STREAKS_ON_HOME] = showStreaksOnHome
    }

    override suspend fun saveSubtitlePreference(showCompletedSubtitle: Boolean) {
        prefs[SHOW_COMPLETED_SUBTITLE] = showCompletedSubtitle
    }

}