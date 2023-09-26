package com.willbsp.habits.data.database.util

import androidx.room.TypeConverter
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

class Converters {

    @TypeConverter
    fun fromDateStamp(date: String): LocalDate {
        return LocalDate.parse(date)
    }

    @TypeConverter
    fun toDateStamp(date: LocalDate): String {
        return date.toString()
    }

    @TypeConverter
    fun fromTimeStamp(time: String): LocalTime {
        return LocalTime.parse(time)
    }

    @TypeConverter
    fun toTimeStamp(time: LocalTime): String {
        return time.toString()
    }

    @TypeConverter
    fun fromDayStamp(day: Int): DayOfWeek {
        return DayOfWeek.of(day)
    }

    @TypeConverter
    fun toDayStamp(day: DayOfWeek): Int {
        return day.value
    }

}