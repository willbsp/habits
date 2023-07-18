package com.willbsp.habits.domain.usecase

import androidx.sqlite.db.SimpleSQLiteQuery
import com.willbsp.habits.data.database.dao.RawDao
import com.willbsp.habits.data.database.util.DatabaseUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.OutputStream
import javax.inject.Inject

class ExportDatabaseUseCase @Inject constructor(
    private val databaseUtils: DatabaseUtils,
    private val rawDao: RawDao
) {

    // Query ensures that a checkpoint is created, so write ahead log is written to main db file
    private val checkpointQuery = SimpleSQLiteQuery("pragma wal_checkpoint(full)")

    suspend operator fun invoke(output: OutputStream) =
        withContext(Dispatchers.IO) {

            val databaseFile = databaseUtils.getDatabasePath()

            if (databaseFile.exists()) {
                rawDao.rawQuery(checkpointQuery)
                output.write(databaseFile.readBytes())
                output.close()
            }

        }

}