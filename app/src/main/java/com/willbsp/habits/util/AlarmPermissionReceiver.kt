package com.willbsp.habits.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmPermissionReceiver : BroadcastReceiver() {

    @Inject
    lateinit var reminderManager: ReminderManager
    override fun onReceive(context: Context, intent: Intent) = goAsync {

        if (intent.action == "android.app.action.SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED") {
            reminderManager.scheduleAllReminders()
        }

    }

}