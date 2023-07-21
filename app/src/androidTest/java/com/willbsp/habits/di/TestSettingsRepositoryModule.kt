package com.willbsp.habits.di

import com.willbsp.habits.data.repository.SettingsRepository
import com.willbsp.habits.fake.repository.FakeSettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton


@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [SettingsRepositoryModule::class]
)
class TestSettingsRepositoryModule {

    @Singleton
    @Provides
    fun provideFakeSettingsRepository(): SettingsRepository {
        return FakeSettingsRepository()
    }


}