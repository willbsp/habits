package com.willbsp.habits.util

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.willbsp.habits.data.repository.EntryRepository
import com.willbsp.habits.data.repository.ReminderRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import java.time.Clock
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class ReminderActionReceiver : BroadcastReceiver() {

    @Inject
    lateinit var reminderRepository: ReminderRepository

    @Inject
    lateinit var entryRepository: EntryRepository

    @Inject
    lateinit var clock: Clock

    override fun onReceive(context: Context, intent: Intent) = goAsync {

        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        val reminderId =
            intent.getIntExtra("reminderId", -1) // make these extra names constant TODO
        if (reminderId != -1) {
            val reminder =
                reminderRepository.getReminderStream(reminderId).first() ?: return@goAsync
            val completed = intent.getBooleanExtra("completed", false)
            entryRepository.setEntry(reminder.habitId, LocalDate.now(clock), completed)
        }

        notificationManager.cancel(reminderId)

    }

}
