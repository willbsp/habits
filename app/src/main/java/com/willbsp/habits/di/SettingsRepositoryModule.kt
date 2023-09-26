package com.willbsp.habits.di

import com.willbsp.habits.data.repository.SettingsRepository
import com.willbsp.habits.data.repository.local.LocalSettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class SettingsRepositoryModule {

    @Binds
    abstract fun bindSettingsRepository(
        localSettingsRepository: LocalSettingsRepository
    ): SettingsRepository

}