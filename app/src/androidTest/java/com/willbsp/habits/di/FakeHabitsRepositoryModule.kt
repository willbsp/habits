package com.willbsp.habits.di

import com.willbsp.habits.data.repo.HabitRepository
import com.willbsp.habits.fake.FakeHabitRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [ViewModelComponent::class],
    replaces = [HabitsRepositoryModule::class]
)
abstract class FakeHabitsRepositoryModule {

    @Binds
    @ViewModelScoped
    abstract fun bindHabitRepository(
        fakeHabitRepository: FakeHabitRepository
    ): HabitRepository

}