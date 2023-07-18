package com.willbsp.habits.fake.dao

import androidx.sqlite.db.SupportSQLiteQuery
import com.willbsp.habits.data.database.dao.RawDao

class FakeRawDao : RawDao {

    override suspend fun rawQuery(supportSQLiteQuery: SupportSQLiteQuery): Int = 1

}