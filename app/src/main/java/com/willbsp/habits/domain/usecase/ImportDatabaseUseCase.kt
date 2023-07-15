package com.willbsp.habits.domain.usecase

import android.content.Context
import android.net.Uri
import com.willbsp.habits.common.DATABASE_NAME
import com.willbsp.habits.data.database.HabitDatabase
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class ImportDatabaseUseCase @Inject constructor(
    @ApplicationContext val context: Context,
    private var habitDatabase: HabitDatabase,
) {

    operator fun invoke(sourceUri: Uri) {

        val database = context.getDatabasePath(DATABASE_NAME)
        val contentResolver = context.contentResolver

        if (database.exists()) {
            val input = contentResolver.openInputStream(sourceUri)
            if (input != null) {

                habitDatabase.close()
                //  TODO carry out null checks
                if (!habitDatabase.isOpen) {
                    val databaseDirectory = File(database.parent)
                    if (databaseDirectory.isDirectory) {
                        for (child: File in databaseDirectory.listFiles()) {
                            child.deleteRecursively()
                        }
                    }
                    database.writeBytes(input.readBytes())
                }
                input.close()

                // TODO fix issues with invalid flows after restart

            }

        }

    }

}