package com.willbsp.habits.domain

import com.willbsp.habits.data.repository.EntryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Clock
import java.time.LocalDate
import javax.inject.Inject

class CalculateStreakUseCase @Inject constructor(
    private val entryRepository: EntryRepository,
    private val clock: Clock
) {

    operator fun invoke(habitId: Int): Flow<Int?> {

        val date = LocalDate.now(clock)
        val yesterday = date.minusDays(1)

        return entryRepository.getAllEntriesStream(habitId).map { list ->
            val entries = list.sortedByDescending { it.date }
            var streak = 0
            if (entries.isNotEmpty()) entries.forEach { entry ->
                if (entry.date == yesterday.minusDays(streak.toLong())) streak++
                else return@forEach
            } else {
                return@map null
            }
            if (entries.first().date == date) streak++
            return@map if (streak > 1) streak
            else null
        }


    }

}