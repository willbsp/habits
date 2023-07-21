package com.willbsp.habits.domain.usecase

import com.willbsp.habits.data.database.util.DatabaseUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream
import javax.inject.Inject

class ImportDatabaseUseCase(
    private val databaseUtils: DatabaseUtils,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    @Inject
    constructor(
        databaseUtils: DatabaseUtils,
    ) : this(databaseUtils, Dispatchers.IO)

    suspend operator fun invoke(input: InputStream): Boolean? = withContext(ioDispatcher) {

        val databaseFile = databaseUtils.getDatabasePath()

        if (databaseFile.exists()) {

            val backupDatabaseFile = File.createTempFile("old", ".db")

            databaseUtils.closeDatabase()
            if (!databaseUtils.isDatabaseOpen()) {
                backupDatabaseFile.writeBytes(databaseFile.readBytes())
                deleteDatabaseFiles(databaseFile)
                databaseFile.writeBytes(input.readBytes())
            }

            if (!databaseUtils.isDatabaseValid()) {
                deleteDatabaseFiles(databaseFile)
                databaseFile.writeBytes(backupDatabaseFile.readBytes())
                input.close()
                return@withContext false
            }

            input.close()
            return@withContext true

        }

        return@withContext null

    }

    private fun deleteDatabaseFiles(databaseFile: File) {
        val databaseDirectory = File(databaseFile.parent!!)
        if (databaseDirectory.isDirectory) {
            for (child: File in databaseDirectory.listFiles()!!) {
                if (child.endsWith("-shm") || child.endsWith("-wal")) {
                    child.deleteRecursively()
                }
            }
        }
        databaseFile.deleteRecursively()
    }

}