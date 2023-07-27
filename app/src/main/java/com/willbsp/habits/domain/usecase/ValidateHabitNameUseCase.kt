package com.willbsp.habits.domain.usecase

import com.willbsp.habits.common.HABIT_NAME_MAX_CHARACTER_LIMIT
import com.willbsp.habits.common.HABIT_NAME_MIN_CHARACTER_LIMIT
import javax.inject.Inject

class ValidateHabitNameUseCase @Inject constructor() {

    operator fun invoke(habitName: String): Boolean {
        return (habitName.length in HABIT_NAME_MIN_CHARACTER_LIMIT..HABIT_NAME_MAX_CHARACTER_LIMIT &&
                !habitName.contains("\n"))
    }

}