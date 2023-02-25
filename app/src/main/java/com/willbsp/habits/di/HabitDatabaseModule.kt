package com.willbsp.habits.di

import android.content.Context
import androidx.room.Room
import com.willbsp.habits.common.DATABASE_NAME
import com.willbsp.habits.data.database.HabitDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HabitDatabaseModule {

    @Singleton
    @Provides
    fun provideHabitDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(app, HabitDatabase::class.java, DATABASE_NAME)
        .fallbackToDestructiveMigration() // TODO need to change at some point
        .build()

    @Singleton
    @Provides
    fun provideHabitDao(db: HabitDatabase) = db.habitDao()

    @Singleton
    @Provides
    fun provideEntryDao(db: HabitDatabase) = db.entryDao()

    @Singleton
    @Provides
    fun provideHabitEntryDao(db: HabitDatabase) = db.habitWithEntriesDao()

}