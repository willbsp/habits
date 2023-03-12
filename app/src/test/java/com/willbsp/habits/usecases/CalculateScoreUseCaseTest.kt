package com.willbsp.habits.usecases

import com.willbsp.habits.domain.CalculateScoreUseCase
import com.willbsp.habits.fake.repository.FakeEntryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

class CalculateScoreUseCaseTest {

    private val date = LocalDate.parse("2023-03-10")
    private val time = "T12:00:00Z"
    private lateinit var fakeEntryRepository: FakeEntryRepository
    private lateinit var calculateScoreUseCase: CalculateScoreUseCase

    @Before
    fun setup() {
        val clock = Clock.fixed(Instant.parse(date.toString() + time), ZoneOffset.UTC)
        fakeEntryRepository = FakeEntryRepository()
        calculateScoreUseCase = CalculateScoreUseCase(fakeEntryRepository, clock)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun calculateScore_onCalculate_calculationCorrect() = runTest {

        val correctScore = 37
        val habitId = 2

        fakeEntryRepository.populate()
        val score = calculateScoreUseCase(habitId).first()?.times(100)?.toInt()

        println(fakeEntryRepository.getAllEntriesStream(3).first().toString())
        println(fakeEntryRepository.getEntry(LocalDate.parse("2023-02-13"), 3))

        assertEquals(correctScore, score)

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun calculateScore_whenNoEntries_returnNull() = runTest {
        assertNull(calculateScoreUseCase(2).first())
    }

}