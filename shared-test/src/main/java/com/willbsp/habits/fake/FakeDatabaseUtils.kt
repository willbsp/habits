package com.willbsp.habits.fake

import com.willbsp.habits.data.database.util.DatabaseUtils
import java.io.File

class FakeDatabaseUtils : DatabaseUtils {

    private var databaseOpen = true

    override fun isDatabaseValid(): Boolean {
        return true
    }

    override fun getDatabasePath(): File {
        return File("test")
    }

    override fun closeDatabase() {
        databaseOpen = false
    }

    override fun isDatabaseOpen(): Boolean = databaseOpen

}