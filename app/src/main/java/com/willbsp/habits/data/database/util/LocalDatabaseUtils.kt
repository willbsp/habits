package com.willbsp.habits.data.database.util

import com.willbsp.habits.data.database.HabitDatabase
import javax.inject.Inject

class LocalDatabaseUtils @Inject constructor(
    private val habitDatabase: HabitDatabase
) : DatabaseUtils {

    override fun closeDatabase() {
        if (habitDatabase.isOpen)
            habitDatabase.close()
    }

    override fun isDatabaseOpen(): Boolean {
        return habitDatabase.isOpen
    }

}