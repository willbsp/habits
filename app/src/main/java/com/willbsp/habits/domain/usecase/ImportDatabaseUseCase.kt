package com.willbsp.habits.domain.usecase

import android.content.Context
import android.net.Uri
import androidx.room.Room
import com.willbsp.habits.common.DATABASE_NAME
import com.willbsp.habits.data.database.HabitDatabase
import com.willbsp.habits.data.database.MIGRATION_1_2
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class ImportDatabaseUseCase @Inject constructor(
    @ApplicationContext val context: Context,
    private var habitDatabase: HabitDatabase,
) {

    // TODO move off the main thread
    operator fun invoke(sourceUri: Uri): Boolean? {

        val databaseFile = context.getDatabasePath(DATABASE_NAME)
        val contentResolver = context.contentResolver

        if (databaseFile.exists()) {
            val input = contentResolver.openInputStream(sourceUri)
            if (input != null) {

                val oldDatabaseFile = File.createTempFile("old", ".db")
                habitDatabase.close()
                if (!habitDatabase.isOpen) {
                    oldDatabaseFile.writeBytes(databaseFile.readBytes())
                    deleteDatabaseFiles(databaseFile)
                    databaseFile.writeBytes(input.readBytes())
                }

                try {
                    Room.databaseBuilder(context, HabitDatabase::class.java, DATABASE_NAME)
                        .addMigrations(MIGRATION_1_2)
                        .build().openHelper.writableDatabase
                } catch (e: Exception) {
                    deleteDatabaseFiles(databaseFile)
                    databaseFile.writeBytes(oldDatabaseFile.readBytes())
                    input.close()
                    return false
                }

                input.close()
                return true

            }

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