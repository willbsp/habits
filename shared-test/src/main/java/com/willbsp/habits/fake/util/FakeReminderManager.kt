package com.willbsp.habits.fake.util

import com.willbsp.habits.util.ReminderManager
import java.time.DayOfWeek
import java.time.LocalTime

class FakeReminderManager : ReminderManager {

    override fun scheduleReminder(reminderId: Int, day: DayOfWeek, time: LocalTime) {
        return
    }

    override fun unscheduleReminder(reminderId: Int) {
        return
    }

    override suspend fun scheduleAllReminders() {
        return
    }

    override suspend fun scheduleAllReminders(habitId: Int) {
        return
    }

    override suspend fun unscheduleAllReminders(habitId: Int) {
        return
    }

}