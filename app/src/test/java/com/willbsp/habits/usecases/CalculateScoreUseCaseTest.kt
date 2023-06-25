package com.willbsp.habits.usecases

import com.willbsp.habits.data.TestData.habit3
import com.willbsp.habits.data.TestData.habit4
import com.willbsp.habits.domain.usecase.CalculateScoreUseCase
import com.willbsp.habits.domain.usecase.GetVirtualEntriesUseCase
import com.willbsp.habits.fake.repository.FakeEntryRepository
import com.willbsp.habits.fake.repository.FakeHabitRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

class CalculateScoreUseCaseTest {

    private val date = LocalDate.parse("2023-03-10")
    private val time = "T12:00:00Z"
    private lateinit var entryRepository: FakeEntryRepository
    private lateinit var habitRepository: FakeHabitRepository
    private lateinit var getVirtualEntriesUseCase: GetVirtualEntriesUseCase
    private lateinit var calculateScoreUseCase: CalculateScoreUseCase

    @Before
    fun setup() {
        val clock = Clock.fixed(Instant.parse(date.toString() + time), ZoneOffset.UTC)
        entryRepository = FakeEntryRepository()
        habitRepository = FakeHabitRepository()
        getVirtualEntriesUseCase = GetVirtualEntriesUseCase(habitRepository, entryRepository)
        calculateScoreUseCase = CalculateScoreUseCase(getVirtualEntriesUseCase, clock)
    }

    @Test
    fun calculateScore_onCalculateDaily_calculationCorrect() = runTest {

        val correctScore = 37

        habitRepository.upsertHabit(habit3)
        entryRepository.populate()
        val score = calculateScoreUseCase(habit3.id).first()?.times(100)?.toInt()

        assertEquals(correctScore, score)

    }

    @Test
    fun calculateScore_onCalculationWeeklyCompleted_calculationCorrect() = runTest {

        val correctScore = 24

        habitRepository.upsertHabit(habit4)
        entryRepository.populate2()
        val score = calculateScoreUseCase(habit4.id).first()?.times(100)?.toInt()

        assertEquals(correctScore, score)

    }

    @Test
    fun calculateScore_onCalculationWeeklyCompleted_ignoresDatesAfterToday() = runTest {

        val correctScore = 22

        habitRepository.upsertHabit(habit4)
        entryRepository.toggleEntry(habit4.id, LocalDate.parse("2023-03-07"))
        entryRepository.toggleEntry(habit4.id, LocalDate.parse("2023-03-08"))
        entryRepository.toggleEntry(habit4.id, LocalDate.parse("2023-03-09"))
        val score = calculateScoreUseCase(habit4.id).first()?.times(100)?.toInt()

        assertEquals(correctScore, score)

    }

    @Test
    fun calculateScore_onCalculationWeeklyUncompleted_calculationCorrect() = runTest {

        val correctScore = 6

        habitRepository.upsertHabit(habit4)
        entryRepository.populate2()
        entryRepository.toggleEntry(habit4.id, LocalDate.parse("2023-03-02"))
        val score = calculateScoreUseCase(habit4.id).first()?.times(100)?.toInt()

        assertEquals(correctScore, score)

    }

    @Test
    fun calculateScore_whenNoEntries_returnNull() = runTest {
        assertNull(calculateScoreUseCase(2).first())
    }

    @Test
    fun calculateScore_whenStartDateIsAfterToday_returnNull() = runTest {
        val clock = Clock.fixed(Instant.parse("2023-04-20$time"), ZoneOffset.UTC)
        calculateScoreUseCase = CalculateScoreUseCase(getVirtualEntriesUseCase, clock)
        assertNull(calculateScoreUseCase(2).first())
    }

}