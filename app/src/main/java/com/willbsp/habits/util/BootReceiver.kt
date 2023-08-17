package com.willbsp.habits.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.willbsp.habits.data.repository.ReminderRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var reminderRepository: ReminderRepository

    @Inject
    lateinit var reminderManager: ReminderManager

    override fun onReceive(context: Context, intent: Intent) = goAsync {

        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            reminderRepository.getAllRemindersStream().first().forEach { reminder ->
                reminderManager.scheduleReminder(reminder.id, reminder.day, reminder.time)
            }
        }

    }

}
