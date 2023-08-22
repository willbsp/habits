package com.willbsp.habits.di

import com.willbsp.habits.data.database.util.DatabaseUtils
import com.willbsp.habits.data.database.util.LocalDatabaseUtils
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DatabaseUtilsModule {

    @Binds
    abstract fun bindDatabaseUtils(
        localDatabaseUtils: LocalDatabaseUtils
    ): DatabaseUtils

}