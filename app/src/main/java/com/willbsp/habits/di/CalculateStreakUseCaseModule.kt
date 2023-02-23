package com.willbsp.habits.di

import com.willbsp.habits.domain.CalculateStreakUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class CalculateStreakUseCaseModule {

    @ViewModelScoped
    @Provides
    fun provideCalculateStreakUseCase(): CalculateStreakUseCase {
        return CalculateStreakUseCase()
    }

}