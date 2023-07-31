package com.willbsp.habits.domain.usecase

import com.willbsp.habits.data.model.Reminder
import com.willbsp.habits.data.repository.HabitRepository
import com.willbsp.habits.data.repository.ReminderRepository
import com.willbsp.habits.ui.common.form.HabitFormUiState
import com.willbsp.habits.ui.common.form.HabitReminderType
import com.willbsp.habits.ui.common.form.toHabit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import javax.inject.Inject

class SaveHabitUseCase(
    private val habitRepository: HabitRepository,
    private val reminderRepository: ReminderRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    @Inject
    constructor(
        habitRepository: HabitRepository,
        reminderRepository: ReminderRepository
    ) : this(habitRepository, reminderRepository, Dispatchers.IO)

    // TODO can HabitData be moved to the domain layer, then just used within habitFormUiState?
    suspend operator fun invoke(data: HabitFormUiState.HabitData, habitId: Int? = null) =
        withContext(ioDispatcher) {
            if (habitId == null) {
                val id = habitRepository.insertHabit(data.toHabit()).toInt()
                saveReminders(data, id)
            } else {
                habitRepository.upsertHabit(data.toHabit(habitId))
                reminderRepository.clearReminders(habitId)
                saveReminders(data, habitId)
            }
        }

    private suspend fun saveReminders(data: HabitFormUiState.Data, habitId: Int) {
        when (data.reminderType) {
            HabitReminderType.NONE -> return

            HabitReminderType.EVERYDAY -> {
                for (day in DayOfWeek.values()) {
                    val reminder = Reminder(
                        habitId = habitId,
                        time = data.reminderTime,
                        day = day.value
                    )
                    reminderRepository.insertReminder(reminder)
                }
            }

            HabitReminderType.SPECIFIC -> {
                for (day in data.reminderDays) {
                    val reminder = Reminder(
                        habitId = habitId,
                        time = data.reminderTime,
                        day = day
                    )
                    reminderRepository.insertReminder(reminder)
                }
            }
        }
    }

}