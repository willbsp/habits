package com.willbsp.habits.di

import com.willbsp.habits.data.repo.*
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

    @Binds
    @ViewModelScoped
    abstract fun bindHabitWithEntriesRepository(
        localHabitWithEntriesRepository: LocalHabitWithEntriesRepository
    ): HabitWithEntriesRepository

}