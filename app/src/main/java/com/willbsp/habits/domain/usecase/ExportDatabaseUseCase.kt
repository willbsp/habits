package com.willbsp.habits.domain.usecase

import android.content.Context
import android.net.Uri
import androidx.sqlite.db.SimpleSQLiteQuery
import com.willbsp.habits.common.DATABASE_NAME
import com.willbsp.habits.data.database.dao.RawDao
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExportDatabaseUseCase @Inject constructor(
    @ApplicationContext val context: Context,
    private val rawDao: RawDao
) {

    // Query ensures that a checkpoint is created, so write ahead log is written to main db file
    private val checkpointQuery = SimpleSQLiteQuery("pragma wal_checkpoint(full)")

    suspend operator fun invoke(destinationUri: Uri) = withContext(Dispatchers.IO) {

        val databaseFile = context.getDatabasePath(DATABASE_NAME)
        val contentResolver = context.contentResolver

        if (databaseFile.exists()) {
            rawDao.rawQuery(checkpointQuery)
            val output = contentResolver.openOutputStream(destinationUri)
            if (output != null) {
                output.write(databaseFile.readBytes())
                output.close()
            }
        }

    }

}