package com.willbsp.habits.domain

import com.willbsp.habits.data.repo.EntryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.time.Clock
import java.time.LocalDate
import javax.inject.Inject

class CalculateStreakUseCase @Inject constructor(
    private val entryRepository: EntryRepository,
    private val clock: Clock
) {

    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO

    suspend operator fun invoke(habitId: Int): Int? = withContext(defaultDispatcher) {
        val entries = entryRepository.getEntriesForHabit(habitId)
            .first().sortedByDescending { it.date }
        val date = LocalDate.now(clock)
        val yesterday = date.minusDays(1)

        var streak = 0
        if (entries.isNotEmpty()) entries.forEach { entry ->
            if (entry.date == yesterday.minusDays(streak.toLong())) streak++
            else return@forEach
        } else {
            return@withContext null
        }

        if (entries.first().date == date) streak++

        return@withContext if (streak > 1) streak
        else null
    }

}