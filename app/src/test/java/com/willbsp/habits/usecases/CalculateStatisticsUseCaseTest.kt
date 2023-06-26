package com.willbsp.habits.usecases

import com.willbsp.habits.data.TestData.entry3
import com.willbsp.habits.domain.usecase.CalculateStatisticsUseCase
import com.willbsp.habits.fake.repository.FakeEntryRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class CalculateStatisticsUseCaseTest {

    private lateinit var fakeEntryRepository: FakeEntryRepository
    private lateinit var calculateStatisticsUseCase: CalculateStatisticsUseCase

    @Before
    fun setup() {
        fakeEntryRepository = FakeEntryRepository()
        calculateStatisticsUseCase = CalculateStatisticsUseCase(fakeEntryRepository)
    }

    @Test
    fun calculateStatistics_whenTotal_calculateTotal() = runTest {
        val expected = 11
        fakeEntryRepository.populate()
        val total = calculateStatisticsUseCase(entry3.habitId).first().total
        assertEquals(expected, total)
    }

    @Test
    fun calculateStatistics_whenNoEntries_totalIsZero() = runTest {
        val total = calculateStatisticsUseCase(entry3.habitId).first().total
        assertEquals(0, total)
    }

    @Test
    fun calculateStatistics_whenEntries_returnOldest() = runTest {
        fakeEntryRepository.populate()
        val started = calculateStatisticsUseCase(entry3.habitId).first().started
        assertEquals(LocalDate.parse("2023-02-11"), started)
    }

    @Test
    fun calculateStatistics_whenNoEntries_returnNull() = runTest {
        val started = calculateStatisticsUseCase(entry3.habitId).first().started
        assertNull(started)
    }

}