package com.willbsp.habits.viewmodel

import com.willbsp.habits.fake.FakeDataSource
import com.willbsp.habits.fake.FakeOfflineHabitRepository
import com.willbsp.habits.rules.TestDispatcherRule
import com.willbsp.habits.ui.home.HomeHabitUiState
import com.willbsp.habits.ui.home.HomeScreenViewModel
import com.willbsp.habits.ui.home.HomeUiState
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HomeScreenViewModelTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    private val time = "2023-04-13T10:30:45Z"
    private val clock: Clock =
        Clock.fixed(Instant.parse(time), Clock.systemDefaultZone().zone)

    private lateinit var fakeRepository: FakeOfflineHabitRepository
    private lateinit var homeViewModel: HomeScreenViewModel

    @Before
    fun createViewModel() {
        fakeRepository = FakeOfflineHabitRepository()
        homeViewModel = HomeScreenViewModel(
            habitsRepository = fakeRepository, clock = clock
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun viewModelHomeUiState_verifyHomeUiStateInitialValue() = runTest {

        val initialHomeUiState = HomeUiState(
            buildList {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val date = LocalDateTime.now(clock).format(formatter)
                FakeDataSource.habitTable.forEach { habit ->
                    var completed = false
                    FakeDataSource.entryTable.forEach { entry ->
                        if (habit.id == entry.habitId && entry.date == date) {
                            completed = true
                        }
                    }
                    this.add(HomeHabitUiState(habit.id, habit.name, completed))
                }
            }
        )

        val job = launch(UnconfinedTestDispatcher()) {
            homeViewModel.homeUiState.collect()
        }

        fakeRepository.emit()
        assertEquals(initialHomeUiState, homeViewModel.homeUiState.value)

        job.cancel()

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun viewModelToggleEntry_createsEntryForCurrentDay() = runTest {

        val job = launch(UnconfinedTestDispatcher()) {
            homeViewModel.homeUiState.collect()
        }

        homeViewModel.toggleEntry(habitId = 1)
        fakeRepository.emit()

        var found = false
        homeViewModel.homeUiState.value.state.forEach {
            if (it.id == 1 && it.completed) {
                found = true
            }
        }
        assertTrue(found)

        job.cancel()

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun viewModelToggleEntry_deletesEntryForCurrentDay() = runTest {

        val job = launch(UnconfinedTestDispatcher()) {
            homeViewModel.homeUiState.collect()
        }

        homeViewModel.toggleEntry(habitId = 2)
        fakeRepository.emit()

        var found = false
        homeViewModel.homeUiState.value.state.forEach {
            if (it.id == 2 && it.completed) {
                found = true
            }
        }
        assertFalse(found)

        job.cancel()

    }

}