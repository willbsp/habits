package com.willbsp.habits.viewmodel

import com.willbsp.habits.TestData.habit1
import com.willbsp.habits.TestData.habit2
import com.willbsp.habits.TestData.habit3
import com.willbsp.habits.domain.CalculateStreakUseCase
import com.willbsp.habits.fake.FakeEntryRepository
import com.willbsp.habits.fake.FakeHabitRepository
import com.willbsp.habits.fake.FakeHabitWithEntriesRepository
import com.willbsp.habits.fake.FakeSettingsRepository
import com.willbsp.habits.rules.TestDispatcherRule
import com.willbsp.habits.ui.screens.home.HabitState
import com.willbsp.habits.ui.screens.home.HomeCompletedUiState
import com.willbsp.habits.ui.screens.home.HomeUiState
import com.willbsp.habits.ui.screens.home.HomeViewModel
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

class HomeViewModelTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    private val date = "2023-03-10"
    private val time = "T12:00:00Z"
    private val entryRepository = FakeEntryRepository()
    private val habitRepository = FakeHabitRepository()
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {

        val clock = Clock.fixed(Instant.parse(date + time), ZoneOffset.UTC)
        val calculateStreakUseCase = CalculateStreakUseCase(entryRepository, clock)
        val fakeHabitWithEntriesRepository =
            FakeHabitWithEntriesRepository(habitRepository, entryRepository)

        viewModel = HomeViewModel(
            entryRepository = entryRepository,
            calculateStreak = calculateStreakUseCase,
            clock = clock,
            habitRepository = fakeHabitWithEntriesRepository,
            settingsRepository = FakeSettingsRepository()
        )

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun uiState_whenInitialised_thenEmpty() = runTest {
        assertEquals(HomeUiState(), viewModel.uiState.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun uiState_whenHabitsAdded_thenShowHabits() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        habitRepository.addHabit(habit1)
        assertTrue(viewModel.uiState.value.habits.any { it.name == habit1.name })
        collectJob.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun uiState_whenDateToggled_thenShowCompleted() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        val expectedCompleted = HomeCompletedUiState(LocalDate.parse(date), true)
        val expectedUncompleted = HomeCompletedUiState(LocalDate.parse(date), false)
        habitRepository.addHabit(habit1)
        entryRepository.toggleEntry(habit1.id, LocalDate.parse(date))
        assertEquals(expectedCompleted,
            viewModel.uiState.value.habits
                .find { it.name == habit1.name }
                ?.completedDates
                ?.find { it.date == expectedCompleted.date }
        )
        entryRepository.toggleEntry(habit1.id, LocalDate.parse(date))
        assertEquals(expectedUncompleted,
            viewModel.uiState.value.habits
                .find { it.name == habit1.name }
                ?.completedDates
                ?.find { it.date == expectedUncompleted.date }
        )
        collectJob.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun uiState_whenMultipleCompleted_thenShowCompletedCount() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        val expected = 2
        habitRepository.addHabit(habit1)
        habitRepository.addHabit(habit2)
        viewModel.toggleEntry(habit1.id, LocalDate.parse(date))
        viewModel.toggleEntry(habit2.id, LocalDate.parse(date))
        assertEquals(expected, viewModel.uiState.value.completedCount)
        collectJob.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun uiState_whenStreak_thenShowStreak() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        val expected = 5
        habitRepository.addHabit(habit3)
        entryRepository.populate()
        assertEquals(
            expected,
            viewModel.uiState.value.habits.find { it.name == habit3.name }?.streak
        )
        collectJob.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun uiState_whenNoHabits_thenNoHabitsState() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        habitRepository.addHabit(habit1)
        assertEquals(HabitState.SHOW_HABITS, viewModel.uiState.value.habitState)
        habitRepository.deleteHabit(habit1.id)
        assertEquals(HabitState.NO_HABITS, viewModel.uiState.value.habitState)
        collectJob.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun uiState_whenAllCompleted_thenAllCompletedState() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        habitRepository.addHabit(habit1)
        habitRepository.addHabit(habit2)
        assertEquals(HabitState.SHOW_HABITS, viewModel.uiState.value.habitState)
        entryRepository.toggleEntry(habit1.id, LocalDate.parse(date))
        entryRepository.toggleEntry(habit2.id, LocalDate.parse(date))
        assertEquals(HabitState.ALL_COMPLETED, viewModel.uiState.value.habitState)
        collectJob.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun toggleEntry_whenDateToggled_thenModifyEntry() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        habitRepository.addHabit(habit1)
        viewModel.toggleEntry(habit1.id, LocalDate.parse(date))
        assertNotNull(entryRepository.getEntry(LocalDate.parse(date), habit1.id))
        viewModel.toggleEntry(habit1.id, LocalDate.parse(date))
        assertNull(entryRepository.getEntry(LocalDate.parse(date), habit1.id))
        collectJob.cancel()
    }

}