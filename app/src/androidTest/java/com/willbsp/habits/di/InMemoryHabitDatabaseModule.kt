package com.willbsp.habits.di

import android.content.Context
import androidx.room.Room
import com.willbsp.habits.data.database.HabitDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
class InMemoryHabitDatabaseModule {

    @Singleton
    @Provides
    fun provideInMemoryDatabase(
        @ApplicationContext app: Context
    ) = Room.inMemoryDatabaseBuilder(app, HabitDatabase::class.java)
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideHabitDao(db: HabitDatabase) = db.habitDao()

    @Singleton
    @Provides
    fun provideEntryDao(db: HabitDatabase) = db.entryDao()

}