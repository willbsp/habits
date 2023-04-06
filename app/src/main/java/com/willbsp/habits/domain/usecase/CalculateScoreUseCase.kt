package com.willbsp.habits.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Clock
import java.time.LocalDate
import javax.inject.Inject

class CalculateScoreUseCase @Inject constructor(
    private val getVirtualEntries: GetVirtualEntriesUseCase,
    private val clock: Clock
) {

    operator fun invoke(habitId: Int): Flow<Float?> {

        return getVirtualEntries(habitId).map { list ->

            if (list.isEmpty())
                return@map null

            val startDate: LocalDate = list.sortedByDescending { it.date }.last().date

            var date = startDate
            var previous = 0f
            while (date != LocalDate.now(clock)) {

                previous = if (list.any { it.habitId == habitId && it.date == date }) {
                    singleExponentialSmoothing(1f, previous)
                } else {
                    singleExponentialSmoothing(0f, previous)
                }
                date = date.plusDays(1f.toLong())
            }

            return@map previous

        }

    }

    private fun singleExponentialSmoothing(
        current: Float,
        previous: Float
    ): Float {
        return (ALPHA * current) + ((1 - ALPHA) * previous)
    }

    companion object {
        const val ALPHA = 0.05f
    }

}