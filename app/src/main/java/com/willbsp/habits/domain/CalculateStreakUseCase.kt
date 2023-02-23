package com.willbsp.habits.domain

import com.willbsp.habits.data.model.HabitWithEntries
import java.time.LocalDate

class CalculateStreakUseCase {

    operator fun invoke(habit: HabitWithEntries, currentDate: LocalDate): Int? {
        return habit.calculateStreak(currentDate)
    }

    private fun HabitWithEntries.calculateStreak(date: LocalDate): Int? {

        val yesterday = date.minusDays(1)
        val entries = this.entries.sortedByDescending { it.date }

        var streak = 0
        if (entries.isNotEmpty()) entries.forEach { entry ->
            if (entry.date == yesterday.minusDays(streak.toLong())) streak++
            else return@forEach
        } else {
            return null
        }

        if (entries.first().date == date) streak++

        return if (streak > 1) streak
        else null

    }

}