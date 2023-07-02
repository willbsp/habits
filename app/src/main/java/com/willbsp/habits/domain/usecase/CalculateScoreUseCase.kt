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

            val today = LocalDate.now(clock)
            val entries = list
                .sortedByDescending { it.date }
                .filter { !it.date.isAfter(LocalDate.now(clock)) }
            if (entries.isEmpty())
                return@map null

            val startDate: LocalDate = entries.last().date
            var date = startDate
            var previous = 0f
            while (date != today.plusDays(1)) {

                if (list.any { it.habitId == habitId && it.date == date }) {
                    previous = singleExponentialSmoothing(1f, previous)
                } else {
                    if (date != today) // score should not be penalised for not being completed today
                        previous = singleExponentialSmoothing(0f, previous)
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