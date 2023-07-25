package com.willbsp.habits.data.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.willbsp.habits.data.database.dao.EntryDao
import com.willbsp.habits.data.database.dao.HabitDao
import com.willbsp.habits.data.database.dao.HabitWithEntriesDao
import com.willbsp.habits.data.database.dao.RawDao
import com.willbsp.habits.data.database.dao.ReminderDao
import com.willbsp.habits.data.database.util.Converters
import com.willbsp.habits.data.model.Entry
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.model.Reminder

@Database(
    entities = [Habit::class, Entry::class, Reminder::class],
    version = 4,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4)
    ]
)
@TypeConverters(Converters::class)
abstract class HabitDatabase : RoomDatabase() {

    abstract fun habitDao(): HabitDao
    abstract fun entryDao(): EntryDao
    abstract fun habitWithEntriesDao(): HabitWithEntriesDao
    abstract fun reminderDao(): ReminderDao
    abstract fun rawDao(): RawDao

}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE INDEX IF NOT EXISTS index_entries_date ON entries (date)")
    }
}