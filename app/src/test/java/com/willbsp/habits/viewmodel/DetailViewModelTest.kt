package com.willbsp.habits.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.willbsp.habits.TestData.habit3
import com.willbsp.habits.domain.usecase.CalculateScoreUseCase
import com.willbsp.habits.domain.usecase.CalculateStatisticsUseCase
import com.willbsp.habits.domain.usecase.CalculateStreakUseCase
import com.willbsp.habits.domain.usecase.GetVirtualEntriesUseCase
import com.willbsp.habits.fake.repository.FakeEntryRepository
import com.willbsp.habits.fake.repository.FakeHabitRepository
import com.willbsp.habits.rules.TestDispatcherRule
import com.willbsp.habits.ui.screens.detail.DetailUiState
import com.willbsp.habits.ui.screens.detail.DetailViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

class DetailViewModelTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    private val date = LocalDate.parse("2023-03-10")
    private val time = "T12:00:00Z"
    private val habitRepository: FakeHabitRepository = FakeHabitRepository()
    private val entryRepository: FakeEntryRepository = FakeEntryRepository()
    private lateinit var getVirtualEntriesUseCase: GetVirtualEntriesUseCase
    private lateinit var scoreUseCase: CalculateScoreUseCase
    private lateinit var streakUseCase: CalculateStreakUseCase
    private lateinit var statisticsUseCase: CalculateStatisticsUseCase
    private lateinit var viewModel: DetailViewModel

    @Before
    fun setup() {
        val clock = Clock.fixed(Instant.parse(date.toString() + time), ZoneOffset.UTC)
        val savedStateHandle = SavedStateHandle(mapOf("habitId" to habit3.id))
        runBlocking { habitRepository.upsertHabit(habit3) }
        getVirtualEntriesUseCase = GetVirtualEntriesUseCase(habitRepository, entryRepository)
        scoreUseCase = CalculateScoreUseCase(getVirtualEntriesUseCase, clock)
        streakUseCase = CalculateStreakUseCase(getVirtualEntriesUseCase, clock)
        statisticsUseCase = CalculateStatisticsUseCase(entryRepository)
        viewModel = DetailViewModel(
            habitRepository,
            savedStateHandle,
            scoreUseCase,
            streakUseCase,
            statisticsUseCase,
            clock
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun uiState_whenLoaded_loadsDetails() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        entryRepository.populate()
        val expected =
            DetailUiState(habit3.id, habit3.name, 5, 5, LocalDate.parse("2023-02-11"), 11, 37)
        assertEquals(expected, viewModel.uiState.value)
        collectJob.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun uiState_whenEntriesChange_UpdatesDetails() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        entryRepository.populate()
        val expected =
            DetailUiState(habit3.id, habit3.name, 5, 5, LocalDate.parse("2023-02-11"), 11, 37)
        assertEquals(expected, viewModel.uiState.value)
        entryRepository.toggleEntry(habit3.id, LocalDate.parse("2023-03-04"))
        entryRepository.toggleEntry(habit3.id, LocalDate.parse("2023-02-10"))
        val updated =
            DetailUiState(habit3.id, habit3.name, 7, 7, LocalDate.parse("2023-02-10"), 13, 42)
        assertEquals(updated, viewModel.uiState.value)
        collectJob.cancel()
    }

}