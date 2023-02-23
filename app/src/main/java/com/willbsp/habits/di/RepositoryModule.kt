package com.willbsp.habits.di

import com.willbsp.habits.data.repo.EntryRepository
import com.willbsp.habits.data.repo.HabitRepository
import com.willbsp.habits.data.repo.LocalEntryRepository
import com.willbsp.habits.data.repo.LocalHabitRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    @ViewModelScoped
    abstract fun bindHabitRepository(
        localHabitRepository: LocalHabitRepository
    ): HabitRepository

    @Binds
    @ViewModelScoped
    abstract fun bindEntryRepository(
        localEntryRepository: LocalEntryRepository
    ): EntryRepository

}