package com.willbsp.habits.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.willbsp.habits.data.repository.ReminderRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

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

fun BroadcastReceiver.goAsync(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> Unit
) {
    val pendingResult = goAsync()
    CoroutineScope(SupervisorJob()).launch(context) {
        try {
            block()
        } finally {
            pendingResult.finish()
        }
    }
}