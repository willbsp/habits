package com.willbsp.habits.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.willbsp.habits.common.SETTINGS_DATASTORE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SettingsDatastoreModule {

    private val Context.datastore by preferencesDataStore(name = SETTINGS_DATASTORE_NAME)

    @Singleton
    @Provides
    fun provideSettingsDatastore(
        @ApplicationContext app: Context
    ): DataStore<Preferences> {
        return app.datastore
    }

}