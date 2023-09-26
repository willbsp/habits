package com.willbsp.habits.util

import java.time.DayOfWeek
import java.time.LocalTime

interface ReminderManager {

    fun scheduleReminder(reminderId: Int, day: DayOfWeek, time: LocalTime)
    fun unscheduleReminder(reminderId: Int)
    suspend fun scheduleAllReminders()
    suspend fun scheduleAllReminders(habitId: Int)
    suspend fun unscheduleAllReminders(habitId: Int)


}