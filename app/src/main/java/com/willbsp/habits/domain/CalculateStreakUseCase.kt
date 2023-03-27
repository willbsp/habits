package com.willbsp.habits.domain

import com.willbsp.habits.data.repository.EntryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CalculateStreakUseCase @Inject constructor(
    private val entryRepository: EntryRepository
) {

    operator fun invoke(habitId: Int): Flow<List<Streak>> {

        return entryRepository.getAllEntriesStream(habitId).map { list ->

            if (list.isEmpty())
                return@map listOf<Streak>()

            val entries = list.sortedByDescending { it.date }
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