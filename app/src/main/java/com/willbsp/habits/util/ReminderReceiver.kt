package com.willbsp.habits.util

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.willbsp.habits.common.REMINDER_NOTIFICATION_CHANNEL_ID

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        val habitName = intent.getStringExtra("habitName")

        if (habitName != null) {
            notificationManager.sendReminderNotification(context, habitName)
        }

    }

    private fun NotificationManager.sendReminderNotification(
        applicationContext: Context,
        habitName: String
    ) {
        val builder =
            NotificationCompat.Builder(applicationContext, REMINDER_NOTIFICATION_CHANNEL_ID)
                .setContentTitle(habitName)
                .setContentText("Have you completed your habit today?")
                .setSmallIcon(androidx.core.R.drawable.ic_call_answer) // TODO
                .setAutoCancel(true)
        notify(1, builder.build())
    }

}