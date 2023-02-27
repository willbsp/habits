package com.willbsp.habits.di

import com.willbsp.habits.data.repository.*
import com.willbsp.habits.data.repository.local.LocalEntryRepository
import com.willbsp.habits.data.repository.local.LocalHabitRepository
import com.willbsp.habits.data.repository.local.LocalHabitWithEntriesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class DatabaseRepositoryModule {

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