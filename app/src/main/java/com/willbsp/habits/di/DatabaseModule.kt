package com.willbsp.habits.di

import android.content.Context
import androidx.room.Room
import com.willbsp.habits.common.DATABASE_NAME
import com.willbsp.habits.data.database.HabitDatabase
import com.willbsp.habits.data.database.MIGRATION_1_2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class DatabaseModule {

    @ActivityRetainedScoped
    @Provides
    fun provideHabitDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(app, HabitDatabase::class.java, DATABASE_NAME)
        .addMigrations(MIGRATION_1_2).build()

    @ActivityRetainedScoped
    @Provides
    fun provideHabitDao(db: HabitDatabase) = db.habitDao()

    @ActivityRetainedScoped
    @Provides
    fun provideEntryDao(db: HabitDatabase) = db.entryDao()

    @ActivityRetainedScoped
    @Provides
    fun provideHabitEntryDao(db: HabitDatabase) = db.habitWithEntriesDao()

    @ActivityRetainedScoped
    @Provides
    fun provideReminderRepository(db: HabitDatabase) = db.reminderDao()

    @ActivityRetainedScoped
    @Provides
    fun provideRawDao(db: HabitDatabase) = db.rawDao()

}