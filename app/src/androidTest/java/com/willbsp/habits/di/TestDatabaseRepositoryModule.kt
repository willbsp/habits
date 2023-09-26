package com.willbsp.habits.di

import com.willbsp.habits.data.repository.EntryRepository
import com.willbsp.habits.data.repository.HabitRepository
import com.willbsp.habits.data.repository.HabitWithEntriesRepository
import com.willbsp.habits.data.repository.ReminderRepository
import com.willbsp.habits.fake.repository.FakeEntryRepository
import com.willbsp.habits.fake.repository.FakeHabitRepository
import com.willbsp.habits.fake.repository.FakeHabitWithEntriesRepository
import com.willbsp.habits.fake.repository.FakeReminderRepository
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
class TestDatabaseRepositoryModule {

    val entryRepository = FakeEntryRepository()
    val habitRepository = FakeHabitRepository()
    val reminderRepository = FakeReminderRepository()

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

    @Singleton
    @Provides
    fun provideFakeReminderRepository(): ReminderRepository {
        return reminderRepository
    }

}