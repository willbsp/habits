package com.willbsp.habits.di

import com.willbsp.habits.data.repository.EntryRepository
import com.willbsp.habits.data.repository.HabitRepository
import com.willbsp.habits.data.repository.HabitWithEntriesRepository
import com.willbsp.habits.fake.repository.FakeEntryRepository
import com.willbsp.habits.fake.repository.FakeHabitRepository
import com.willbsp.habits.fake.repository.FakeHabitWithEntriesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseRepositoryModule::class]
)
class HabitRepositoryTestModule {

    val entryRepository = FakeEntryRepository()
    val habitRepository = FakeHabitRepository()

    @Singleton
    @Provides
    fun provideFakeHabitRepository(): HabitRepository {
        return habitRepository
    }

    @Singleton
    @Provides
    fun provideFakeEntryRepository(): EntryRepository {
        return entryRepository
    }

    @Singleton
    @Provides
    fun provideFakeHabitWithEntriesRepository(): HabitWithEntriesRepository {
        return FakeHabitWithEntriesRepository(habitRepository, entryRepository)
    }

}