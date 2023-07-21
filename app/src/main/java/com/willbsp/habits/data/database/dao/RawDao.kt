package com.willbsp.habits.data.database.dao

import androidx.room.Dao
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface RawDao {

    @RawQuery
    suspend fun rawQuery(supportSQLiteQuery: SupportSQLiteQuery): Int

}