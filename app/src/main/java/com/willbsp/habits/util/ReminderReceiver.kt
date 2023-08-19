package com.willbsp.habits.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.willbsp.habits.MainActivity
import com.willbsp.habits.R
import com.willbsp.habits.common.REMINDER_NOTIFICATION_CHANNEL_ID
import com.willbsp.habits.data.repository.EntryRepository
import com.willbsp.habits.data.repository.HabitRepository
import com.willbsp.habits.data.repository.ReminderRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import java.time.Clock
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class ReminderReceiver : BroadcastReceiver() {

    @Inject
    lateinit var reminderRepository: ReminderRepository

    @Inject
    lateinit var habitRepository: HabitRepository

    @Inject
    lateinit var entryRepository: EntryRepository

    @Inject
    lateinit var reminderManager: ReminderManager

    @Inject
    lateinit var clock: Clock

    override fun onReceive(context: Context, intent: Intent) = goAsync {

        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        val reminderId = intent.getIntExtra("reminderId", -1)

        if (reminderId != -1) {

            val reminder = reminderRepository.getReminderStream(reminderId).first()
            val habitName = habitRepository.getHabit(reminder.habitId)?.name

            if (habitName != null) {
                // if habit has not already been completed today
                if (entryRepository.getEntry(LocalDate.now(clock), reminder.habitId) == null) {
                    notificationManager.sendReminderNotification(context, reminderId, habitName)
                }
            }

            reminderManager.scheduleReminder(reminder.id, reminder.day, reminder.time)

        }

    }

    private fun NotificationManager.sendReminderNotification(
        context: Context,
        reminderId: Int,
        habitName: String
    ) {

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder =
            NotificationCompat.Builder(context, REMINDER_NOTIFICATION_CHANNEL_ID)
                .setContentTitle(habitName)
                .setContentText(context.getString(R.string.reminder_notification_desc))
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        notify(reminderId, builder.build())
    }

}