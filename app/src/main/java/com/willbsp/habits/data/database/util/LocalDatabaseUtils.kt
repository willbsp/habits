package com.willbsp.habits.data.database.util

import android.content.Context
import androidx.room.Room
import com.willbsp.habits.common.DATABASE_NAME
import com.willbsp.habits.data.database.HabitDatabase
import com.willbsp.habits.data.database.MIGRATION_1_2
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class LocalDatabaseUtils @Inject constructor(
    @ApplicationContext val context: Context,
    private val habitDatabase: HabitDatabase
) : DatabaseUtils {

    override fun isDatabaseValid(): Boolean {
        val testDatabase: HabitDatabase =
            Room.databaseBuilder(context, HabitDatabase::class.java, DATABASE_NAME)
                .addMigrations(MIGRATION_1_2)
                .build()
        try {
            testDatabase.openHelper.writableDatabase
        } catch (e: Exception) {
            testDatabase.close()
            return false
        }
        testDatabase.close()
        return true
    }

    override fun getDatabasePath(): File {
        return context.getDatabasePath(DATABASE_NAME)
    }

    override fun closeDatabase() {
        if (habitDatabase.isOpen)
            habitDatabase.close()
    }

    override fun isDatabaseOpen(): Boolean {
        return habitDatabase.isOpen
    }

}