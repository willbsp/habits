package com.willbsp.habits.di

import com.willbsp.habits.data.repository.*
import com.willbsp.habits.data.repository.local.LocalEntryRepository
import com.willbsp.habits.data.repository.local.LocalHabitRepository
import com.willbsp.habits.data.repository.local.LocalHabitWithEntriesRepository
import com.willbsp.habits.data.repository.local.LocalReminderRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseRepositoryModule {

    @Binds
    abstract fun bindHabitRepository(
        localHabitRepository: LocalHabitRepository
    ): HabitRepository

    @Binds
    abstract fun bindEntryRepository(
        localEntryRepository: LocalEntryRepository
    ): EntryRepository

    @Binds
    abstract fun bindHabitWithEntriesRepository(
        localHabitWithEntriesRepository: LocalHabitWithEntriesRepository
    ): HabitWithEntriesRepository

    @Binds
    abstract fun bindReminderRepository(
        localReminderRepository: LocalReminderRepository
    ): ReminderRepository

}