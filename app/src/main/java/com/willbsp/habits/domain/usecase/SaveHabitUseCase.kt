package com.willbsp.habits.domain.usecase

import com.willbsp.habits.common.HABIT_NAME_MAX_CHARACTER_LIMIT
import com.willbsp.habits.common.HABIT_NAME_MIN_CHARACTER_LIMIT
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.repository.HabitRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class SaveHabitUseCase @Inject constructor(
    private val habitRepository: HabitRepository,
) {

    operator fun invoke(habit: Habit, coroutineScope: CoroutineScope): Boolean {
        return if (habit.name.length in (HABIT_NAME_MIN_CHARACTER_LIMIT + 1)..HABIT_NAME_MAX_CHARACTER_LIMIT) {
            coroutineScope.launch { habitRepository.upsertHabit(habit) }
            true
        } else {
            false
        }
    }

}