package com.willbsp.habits.domain.usecase

import androidx.sqlite.db.SimpleSQLiteQuery
import com.willbsp.habits.data.database.dao.RawDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.OutputStream
import javax.inject.Inject

class ExportDatabaseUseCase @Inject constructor(
    private val rawDao: RawDao
) {

    // Query ensures that a checkpoint is created, so write ahead log is written to main db file
    private val checkpointQuery = SimpleSQLiteQuery("pragma wal_checkpoint(full)")

    suspend operator fun invoke(databaseFile: File, output: OutputStream) =
        withContext(Dispatchers.IO) {

            if (databaseFile.exists()) {
                rawDao.rawQuery(checkpointQuery)
                output.write(databaseFile.readBytes())
                output.close()
            }

        }

}