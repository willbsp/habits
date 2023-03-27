package com.willbsp.habits.usecases

import com.willbsp.habits.domain.CalculateStreakUseCase
import com.willbsp.habits.fake.repository.FakeEntryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class CalculateStreakUseCaseTest {

    private val date = LocalDate.parse("2023-03-10")

    // private val time = "T12:00:00Z"
    private lateinit var fakeEntryRepository: FakeEntryRepository
    private lateinit var calculateStreakUseCase: CalculateStreakUseCase

    // TODO need tests for multiple streaks

    @Before
    fun setup() {
        // val clock = Clock.fixed(Instant.parse(date.toString() + time), ZoneOffset.UTC)
        fakeEntryRepository = FakeEntryRepository()
        calculateStreakUseCase = CalculateStreakUseCase(fakeEntryRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun calculateStreak_whenStreak_streakCalculated() = runTest {
        val correctStreak = 5
        val habitId = 2
        fakeEntryRepository.populate()
        val streak = calculateStreakUseCase(habitId).first()
        assertEquals(correctStreak, streak.first().length)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun calculateStreak_whenNoStreak_returnEmptyList() = runTest {
        val habitId = 2
        fakeEntryRepository.toggleEntry(2, date.minusDays(3f.toLong()))
        fakeEntryRepository.toggleEntry(2, date.minusDays(6f.toLong()))
        fakeEntryRepository.toggleEntry(2, date.minusDays(8f.toLong()))
        assertTrue(calculateStreakUseCase(habitId).first().isEmpty())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun calculateStreak_whenEmptyList_returnEmptyList() = runTest {
        assertTrue(calculateStreakUseCase(2).first().isEmpty())
    }

}