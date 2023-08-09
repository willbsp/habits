package com.willbsp.habits.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import java.time.Clock

@Module
@InstallIn(ViewModelComponent::class)
class ClockModule {

    @Provides
    fun provideClock(): Clock = Clock.systemDefaultZone()

}