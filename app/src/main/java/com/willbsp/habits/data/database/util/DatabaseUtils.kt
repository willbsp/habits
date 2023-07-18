package com.willbsp.habits.data.database.util

interface DatabaseUtils {

    fun closeDatabase()

    fun isDatabaseOpen(): Boolean

}