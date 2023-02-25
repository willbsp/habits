package com.willbsp.habits.domain

import com.willbsp.habits.data.repo.EntryRepository
import java.time.Clock
import java.time.LocalDate
import java.time.Period
import javax.inject.Inject

class CalculateScoreUseCase @Inject constructor(
    val entryRepository: EntryRepository,
    val clock: Clock
) {

    suspend operator fun invoke(habitId: Int): Float? {

        val oldestEntry = entryRepository.getOldestEntry(habitId) ?: return null
        val series = buildSeries(oldestEntry.date, habitId)
        return calculateScore(series)

    }

    private suspend fun buildSeries(
        startDate: LocalDate,
        habitId: Int
    ): List<Int> {

        val period = Period.between(startDate, LocalDate.now(clock).plusDays(1))

        return buildList {

            var date: LocalDate = startDate
            repeat(period.days) {
                if (entryRepository.getEntryForDate(date, habitId) != null) this.add(1)
                else this.add(0)
                date = date.plusDays(1)
            }

        }

    }

    private fun calculateScore(
        series: List<Int>
    ): Float {

        var previous = 0f
        series.forEach { current ->
            previous = singleExponentialSmoothing(current.toFloat(), previous)
        }
        return previous

    }

    private fun singleExponentialSmoothing(
        current: Float,
        previous: Float
    ): Float {
        return ALPHA * current + (1 - ALPHA) * previous
    }

    companion object {
        const val ALPHA = 0.05f
    }

}