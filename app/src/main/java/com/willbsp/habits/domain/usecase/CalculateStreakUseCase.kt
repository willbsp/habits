package com.willbsp.habits.domain.usecase

import com.willbsp.habits.domain.model.Streak
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Clock
import java.time.LocalDate
import javax.inject.Inject

class CalculateStreakUseCase @Inject constructor(
    private val getVirtualEntriesUseCase: GetVirtualEntriesUseCase,
    private val clock: Clock
) {

    operator fun invoke(habitId: Int): Flow<List<Streak>> {

        return getVirtualEntriesUseCase(habitId).map { list ->

            if (list.isEmpty())
                return@map listOf<Streak>()

            val today = LocalDate.now(clock)
            val entries = list
                .sortedByDescending { it.date }
                .filter { !it.date.isAfter(today) }
            val streaks = mutableListOf<Streak>()

            var streak = 0
            var endDate = entries.first().date
            var lastDate = entries.first().date.plusDays(1)

            entries.forEach { entry ->

                if (entry.date.plusDays(1) == lastDate) streak++
                else {
                    if (streak > 1) streaks.add(Streak(streak, endDate))
                    endDate = entry.date
                    streak = 1
                }
                lastDate = entry.date

            }
            if (streak > 1) streaks.add(Streak(streak, endDate))

            return@map streaks

        }


    }

}