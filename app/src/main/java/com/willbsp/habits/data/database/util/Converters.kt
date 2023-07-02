package com.willbsp.habits.data.database.util

import androidx.room.TypeConverter
import java.time.LocalDate

class Converters {

    @TypeConverter
    fun fromDateStamp(date: String): LocalDate {
        return LocalDate.parse(date)
    }

    @TypeConverter
    fun toDateStamp(date: LocalDate): String {
        return date.toString()
    }

}