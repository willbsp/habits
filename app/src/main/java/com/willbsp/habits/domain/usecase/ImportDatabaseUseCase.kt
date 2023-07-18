package com.willbsp.habits.domain.usecase

import com.willbsp.habits.data.database.util.DatabaseUtils
import java.io.File
import java.io.InputStream
import javax.inject.Inject

class ImportDatabaseUseCase @Inject constructor(
    private val databaseUtils: DatabaseUtils
) {

    // TODO move off the main thread
    operator fun invoke(input: InputStream): Boolean? {

        val databaseFile = databaseUtils.getDatabasePath()

        if (databaseFile.exists()) {

            val backupDatabaseFile = File.createTempFile("old", ".db")

            databaseUtils.closeDatabase()
            if (!databaseUtils.isDatabaseOpen()) {
                backupDatabaseFile.writeBytes(databaseFile.readBytes())
                deleteDatabaseFiles(databaseFile)
                databaseFile.writeBytes(input.readBytes())
            }

            if (!databaseUtils.validateDatabase()) {
                deleteDatabaseFiles(databaseFile)
                databaseFile.writeBytes(backupDatabaseFile.readBytes())
                input.close()
                return false
            }

            input.close()
            return true

        }

        return null

    }

    private fun deleteDatabaseFiles(databaseFile: File) {
        val databaseDirectory = File(databaseFile.parent!!)
        if (databaseDirectory.isDirectory) {
            for (child: File in databaseDirectory.listFiles()!!) {
                child.deleteRecursively()
            }
        }
    }

}