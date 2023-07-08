package com.willbsp.habits.viewmodel

import com.willbsp.habits.data.TestData.habit1
import com.willbsp.habits.data.TestData.habit2
import com.willbsp.habits.data.TestData.habit3
import com.willbsp.habits.domain.usecase.CalculateScoreUseCase
import com.willbsp.habits.domain.usecase.CalculateStreakUseCase
import com.willbsp.habits.domain.usecase.GetHabitsWithVirtualEntriesUseCase
import com.willbsp.habits.domain.usecase.GetVirtualEntriesUseCase
import com.willbsp.habits.fake.repository.FakeEntryRepository
import com.willbsp.habits.fake.repository.FakeHabitRepository
import com.willbsp.habits.fake.repository.FakeSettingsRepository
import com.willbsp.habits.rules.TestDispatcherRule
import com.willbsp.habits.ui.screens.home.HomeUiState
import com.willbsp.habits.ui.screens.home.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
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

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    private val date = LocalDate.parse("2023-03-10")
    private val time = "T12:00:00Z"
    private val entryRepository = FakeEntryRepository()
    private val habitRepository = FakeHabitRepository()
    private val getVirtualEntriesUseCase =
        GetVirtualEntriesUseCase(habitRepository, entryRepository)
    private val settingsRepository = FakeSettingsRepository()
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {

        val clock = Clock.fixed(Instant.parse(date.toString() + time), ZoneOffset.UTC)
        val calculateStreakUseCase = CalculateStreakUseCase(getVirtualEntriesUseCase, clock)
        val calculateScoreUseCase = CalculateScoreUseCase(getVirtualEntriesUseCase, clock)
        val getHabitsWithVirtualEntries =
            GetHabitsWithVirtualEntriesUseCase(habitRepository, getVirtualEntriesUseCase)

        viewModel = HomeViewModel(
            entryRepository = entryRepository,
            calculateStreak = calculateStreakUseCase,
            calculateScore = calculateScoreUseCase,
            clock = clock,
            getHabitsWithVirtualEntries = getHabitsWithVirtualEntries,
            settingsRepository = settingsRepository
        )

    }

    @Test
    fun uiState_whenInitialised_thenEmpty() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        assertEquals(HomeUiState.Empty, viewModel.uiState.value)
        collectJob.cancel()
    }

    @Test
    fun uiState_whenInitialised_correctDate() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        habitRepository.upsertHabit(habit1)
        val state = viewModel.uiState.value as HomeUiState.Habits
        assertEquals(date, state.todaysDate)
        collectJob.cancel()
    }

    @Test
    fun uiState_whenHabitsAdded_thenShowHabits() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        val habits = viewModel.uiState.map { (it as HomeUiState.Habits).habits }
        habitRepository.upsertHabit(habit1)
        assertTrue(habits.first().any { it.name == habit1.name })
        collectJob.cancel()
    }

    @Test
    fun uiState_whenDateToggled_thenShowCompleted() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        val habits = viewModel.uiState.map { (it as HomeUiState.Habits).habits }
        habitRepository.upsertHabit(habit1)
        entryRepository.toggleEntry(habit1.id, date)
        assertTrue(
            habits.first().find { it.name == habit1.name }?.completed?.contains(date) == true
        )
        entryRepository.toggleEntry(habit1.id, date)
        assertFalse(
            habits.first().find { it.name == habit1.name }?.completed?.contains(date) == true
        )
        collectJob.cancel()
    }

    @Test
    fun uiState_whenStreak_thenShowStreak() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        val habits = viewModel.uiState.map { (it as HomeUiState.Habits).habits }
        val expected = 5
        habitRepository.upsertHabit(habit3)
        entryRepository.populate()
        assertEquals(expected, habits.first().find { it.name == habit3.name }?.streak)
        collectJob.cancel()
    }

    @Test
    fun uiState_whenScore_thenShowScore() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        val habits = viewModel.uiState.map { (it as HomeUiState.Habits).habits }
        val expected = 37
        habitRepository.upsertHabit(habit3)
        entryRepository.populate()
        assertEquals(expected, habits.first().find { it.name == habit3.name }?.score)
        collectJob.cancel()
    }

    @Test
    fun uiState_whenNoHabits_thenNoHabitsState() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        habitRepository.upsertHabit(habit1)
        assertTrue(viewModel.uiState.value is HomeUiState.Habits)
        habitRepository.deleteHabit(habit1.id)
        assertTrue(viewModel.uiState.value is HomeUiState.Empty)
        collectJob.cancel()
    }

    @Test
    fun uiState_whenDateToggledWeekly_thenShowWeeklyCompleted() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        val habits = viewModel.uiState.map { (it as HomeUiState.Habits).habits }
        habitRepository.upsertHabit(habit2)
        entryRepository.toggleEntry(habit2.id, date)
        val habit = habits.first().find { it.name == habit2.name }!!
        assertTrue(habit.completed.contains(date))
        assertTrue(habit.completedByWeek.contains(date.minusDays(1)))
        assertTrue(habit.completedByWeek.contains(date.minusDays(3)))
        collectJob.cancel()
    }

    @Test
    fun uiState_whenDateToggledWeekly_thenShowStreak() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        val habits = viewModel.uiState.map { (it as HomeUiState.Habits).habits }
        habitRepository.upsertHabit(habit2)
        entryRepository.toggleEntry(habit2.id, date)
        val habit = habits.first().find { it.name == habit2.name }!!
        assertEquals(5, habit.streak)
        collectJob.cancel()
    }

    @Test
    fun uiState_whenPreferencesSet_getPreferences() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        settingsRepository.emit()
        val state = viewModel.uiState.map { (it as HomeUiState.Habits) }
        habitRepository.upsertHabit(habit1)
        assertTrue(state.first().showStreaks)
        assertTrue(state.first().showSubtitle)
        assertFalse(state.first().showScore)
        settingsRepository.saveStatisticPreference(false)
        settingsRepository.saveSubtitlePreference(false)
        settingsRepository.saveScorePreference(true)
        assertFalse(state.first().showStreaks)
        assertFalse(state.first().showSubtitle)
        assertTrue(state.first().showScore)
        collectJob.cancel()
    }

    @Test
    fun toggleEntry_whenDateToggled_thenModifyState() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        val habits = viewModel.uiState.map { (it as HomeUiState.Habits).habits }
        habitRepository.upsertHabit(habit1)
        viewModel.toggleEntry(habit1.id, date)
        assertTrue(
            habits.first().find { it.name == habit1.name }?.completed?.contains(date) == true
        )
        viewModel.toggleEntry(habit1.id, date)
        assertFalse(
            habits.first().find { it.name == habit1.name }?.completed?.contains(date) == true
        )
        collectJob.cancel()
    }

}