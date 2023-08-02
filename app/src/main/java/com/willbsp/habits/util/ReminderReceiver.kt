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

        notificationManager.sendReminderNotification(
            applicationContext = context,
            channelId = REMINDER_NOTIFICATION_CHANNEL_ID
        )

        val reminderId = intent.getIntExtra("reminderId", -1)

    }

    private fun NotificationManager.sendReminderNotification(
        applicationContext: Context,
        channelId: String
    ) {
        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("title text")
            .setContentText("body text")
            .setSmallIcon(androidx.core.R.drawable.ic_call_answer)
            .setAutoCancel(true)
        notify(1, builder.build())
    }

}