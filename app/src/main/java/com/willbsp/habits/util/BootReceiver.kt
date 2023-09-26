package com.willbsp.habits.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {


    @Inject
    lateinit var reminderManager: ReminderManager

    override fun onReceive(context: Context, intent: Intent) = goAsync {

        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            reminderManager.scheduleAllReminders()
        }

    }

}
