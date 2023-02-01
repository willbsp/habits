package com.willbsp.habits.di

import com.willbsp.habits.data.repo.LocalSettingsRepository
import com.willbsp.habits.data.repo.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindSettingsRepository(
        localSettingsRepository: LocalSettingsRepository
    ): SettingsRepository

}