package com.willbsp.habits.fake.util

import com.willbsp.habits.data.database.util.DatabaseUtils
import java.io.File

class FakeDatabaseUtils : DatabaseUtils {

    private var databaseOpen = true
    private var databaseValid = true
    private var databaseFile: File

    init {
        databaseFile = File.createTempFile("test", ".db")
        databaseFile.writeText(TEST_FILE_TEXT)
    }

    fun setDatabaseValidity(valid: Boolean) {
        databaseValid = valid
    }

    override fun isDatabaseValid(): Boolean {
        return databaseValid
    }

    override fun getDatabasePath(): File {
        return databaseFile
    }

    override fun closeDatabase() {
        databaseOpen = false
    }

    override fun isDatabaseOpen(): Boolean = databaseOpen

    companion object {
        const val TEST_FILE_TEXT = "this is a test file"
    }

}