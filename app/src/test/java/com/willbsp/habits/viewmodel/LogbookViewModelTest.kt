package com.willbsp.habits.viewmodel

import com.willbsp.habits.TestData.habit1
import com.willbsp.habits.TestData.habit2
import com.willbsp.habits.TestData.habit3
import com.willbsp.habits.fake.repository.FakeEntryRepository
import com.willbsp.habits.fake.repository.FakeHabitRepository
import com.willbsp.habits.fake.repository.FakeHabitWithEntriesRepository
import com.willbsp.habits.rules.TestDispatcherRule
import com.willbsp.habits.ui.screens.logbook.LogbookViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

class LogbookViewModelTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    private val date = LocalDate.parse("2023-03-10")
    private val time = "T12:00:00Z"
    private val habitRepository = FakeHabitRepository()
    private val entryRepository = FakeEntryRepository()
    private lateinit var viewModel: LogbookViewModel

    @Before
    fun setup() {

        val clock = Clock.fixed(Instant.parse(date.toString() + time), ZoneOffset.UTC)
        val habitsWithEntryRepository =
            FakeHabitWithEntriesRepository(habitRepository, entryRepository)

        viewModel = LogbookViewModel(habitsWithEntryRepository, entryRepository, clock)

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun uiState_whenInitialised_thenLoadTodayHabits() = runTest {
        habitRepository.addHabit(habit2)
        entryRepository.toggleEntry(habit2.id, date)
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        assertEquals(habit2.name, viewModel.uiState.value.habits.first().name)
        assertTrue(viewModel.uiState.value.habits.first().completed)
        collectJob.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun setSelectedDate_whenDateSelected_thenLoadHabits() = runTest {
        habitRepository.addHabit(habit3)
        entryRepository.populate()
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        assertFalse(viewModel.uiState.value.habits.first().completed)
        viewModel.setSelectedDate(date.minusDays(1))
        assertTrue(viewModel.uiState.value.habits.first().completed)
        viewModel.setSelectedDate(date.minusDays(2))
        assertTrue(viewModel.uiState.value.habits.first().completed)
        viewModel.setSelectedDate(date.minusDays(3))
        collectJob.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun toggleEntry_whenDateToggled_thenModifyState() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        habitRepository.addHabit(habit1)
        viewModel.toggleEntry(habit1.id, date)
        assertTrue(
            viewModel.uiState.value.habits.find { it.name == habit1.name }?.completed ?: false
        )
        viewModel.toggleEntry(habit1.id, date)
        assertFalse(
            viewModel.uiState.value.habits.find { it.name == habit1.name }?.completed ?: true
        )
        collectJob.cancel()
    }

}