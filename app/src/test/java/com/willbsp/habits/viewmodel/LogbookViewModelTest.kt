package com.willbsp.habits.viewmodel

import com.willbsp.habits.fake.repository.FakeEntryRepository
import com.willbsp.habits.fake.repository.FakeHabitRepository
import com.willbsp.habits.fake.repository.FakeHabitWithEntriesRepository
import com.willbsp.habits.rules.TestDispatcherRule
import com.willbsp.habits.ui.screens.logbook.LogbookViewModel
import org.junit.Rule
import java.time.LocalDate

class LogbookViewModelTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    private val date = LocalDate.parse("2023-03-10")
    private val habitRepository = FakeHabitRepository()
    private val entryRepository = FakeEntryRepository()
    private val habitWithEntriesRepository =
        FakeHabitWithEntriesRepository(habitRepository, entryRepository)
    private lateinit var viewModel: LogbookViewModel

    // TODO FIX

    /*@Before
    fun setup() {
        viewModel = LogbookViewModel(habitWithEntriesRepository, entryRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun uiState_whenEmpty_thenNoHabitsState() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        assertEquals(LogbookUiState.NoHabits, viewModel.uiState.first())
        collectJob.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun uiState_whenInitialised_getFirstHabitAndSetSelectedHabitId() = runTest {
        habitRepository.upsertHabit(habit2)
        entryRepository.toggleEntry(habit2.id, date)
        viewModel = LogbookViewModel(habitWithEntriesRepository, entryRepository)
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        val uiState = viewModel.uiState.map { (it as LogbookUiState.SelectedHabit) }
        assertEquals(habit2.id, uiState.first().habitId)
        collectJob.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun uiState_whenInitialised_getFirstHabitAndLoadEntries() = runTest {
        habitRepository.upsertHabit(habit2)
        entryRepository.toggleEntry(habit2.id, date)
        entryRepository.toggleEntry(habit2.id, date.minusDays(1))
        viewModel = LogbookViewModel(habitWithEntriesRepository, entryRepository)
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        val uiState = viewModel.uiState.map { it as LogbookUiState.SelectedHabit }
        assertTrue(uiState.first().completed.contains(date))
        assertTrue(uiState.first().completed.contains(date.minusDays(1)))
        collectJob.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun uiState_whenHabitSelected_thenLoadHabits() = runTest {
        habitRepository.upsertHabit(habit2)
        habitRepository.upsertHabit(habit3)
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        viewModel.setSelectedHabit(habit2.id)
        val uiState = viewModel.uiState.map { it as LogbookUiState.SelectedHabit }
        assertNotNull(uiState.first().habits.find { it.name == habit2.name })
        assertNotNull(uiState.first().habits.find { it.name == habit3.name })
        collectJob.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun setSelectedHabit_whenSelectedHabitChanges_thenLoadNewEntries() = runTest {
        habitRepository.upsertHabit(habit1)
        habitRepository.upsertHabit(habit2)
        entryRepository.toggleEntry(habit1.id, date)
        entryRepository.toggleEntry(habit2.id, date.plusDays(1))
        viewModel.setSelectedHabit(habit1.id)
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        val uiState = viewModel.uiState.map { it as LogbookUiState.SelectedHabit }
        assertTrue(uiState.first().completed.contains(date))
        viewModel.setSelectedHabit(habit2.id)
        assertTrue(uiState.first().completed.contains(date.plusDays(1)))
        collectJob.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun toggleEntry_whenDateToggled_thenModifyState() = runTest {
        habitRepository.upsertHabit(habit1)
        habitRepository.upsertHabit(habit2)
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        viewModel.setSelectedHabit(habit1.id)
        val uiState = viewModel.uiState.map { it as LogbookUiState.SelectedHabit }
        viewModel.toggleEntry(date)
        assertTrue(uiState.first().completed.contains(date))
        viewModel.setSelectedHabit(habit2.id)
        assertFalse(uiState.first().completed.contains(date))
        viewModel.toggleEntry(date)
        assertTrue(uiState.first().completed.contains(date))
        collectJob.cancel()
    }*/

}