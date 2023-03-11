package com.willbsp.habits.usecases

import com.willbsp.habits.domain.CalculateStreakUseCase
import com.willbsp.habits.fake.FakeEntryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

class CalculateStreakUseCaseTest {

    private val date = "2023-03-10T12:00:00Z"
    private lateinit var fakeEntryRepository: FakeEntryRepository
    private lateinit var calculateStreakUseCase: CalculateStreakUseCase

    @Before
    fun setup() {
        val clock = Clock.fixed(Instant.parse(date), ZoneOffset.UTC)
        fakeEntryRepository = FakeEntryRepository()
        calculateStreakUseCase = CalculateStreakUseCase(fakeEntryRepository, clock)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun calculateStreak_onCalculation_calculationCorrect() = runTest {

        val correctStreak = 5
        val habitId = 2

        fakeEntryRepository.populate()
        val streak = calculateStreakUseCase(habitId).first()

        assertEquals(correctStreak, streak)

    }

}