package com.willbsp.habits.di

import com.willbsp.habits.data.database.util.DatabaseUtils
import com.willbsp.habits.data.database.util.LocalDatabaseUtils
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class DatabaseUtilsModule {

    @Binds
    @ActivityRetainedScoped
    abstract fun bindDatabaseUtils(
        localDatabaseUtils: LocalDatabaseUtils
    ): DatabaseUtils

}