package com.willbsp.habits.util

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.willbsp.habits.common.REMINDER_NOTIFICATION_CHANNEL_ID
import com.willbsp.habits.data.repository.ReminderRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@AndroidEntryPoint
class ReminderReceiver : BroadcastReceiver() {

    @Inject
    lateinit var reminderRepository: ReminderRepository

    @Inject
    lateinit var reminderManager: ReminderManager

    override fun onReceive(context: Context, intent: Intent) = goAsync {

        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        val reminderId = intent.getIntExtra("reminderId", -1)
        val habitName = intent.getStringExtra("habitName")

        if (habitName != null && reminderId != -1) {
            notificationManager.sendReminderNotification(context, reminderId, habitName)
            val reminder = reminderRepository.getReminderStream(reminderId).first()
            reminderManager.scheduleReminder(reminder.id, reminder.day, reminder.time)
        }

    }

    private fun NotificationManager.sendReminderNotification(
        applicationContext: Context,
        reminderId: Int,
        habitName: String
    ) {
        val builder =
            NotificationCompat.Builder(applicationContext, REMINDER_NOTIFICATION_CHANNEL_ID)
                .setContentTitle(habitName)
                .setContentText("Have you completed your habit today?")
                .setSmallIcon(androidx.core.R.drawable.ic_call_answer) // TODO
                .setAutoCancel(true)
        notify(reminderId, builder.build())
    }

}