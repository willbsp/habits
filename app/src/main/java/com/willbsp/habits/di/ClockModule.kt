package com.willbsp.habits.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.Clock
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ClockModule {

    @Singleton
    @Provides
    fun provideClock(): Clock = Clock.systemDefaultZone()

}