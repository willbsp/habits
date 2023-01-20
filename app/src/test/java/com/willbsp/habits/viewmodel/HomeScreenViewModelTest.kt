package com.willbsp.habits.viewmodel

import com.willbsp.habits.TestData.habit1
import com.willbsp.habits.TestData.habit2
import com.willbsp.habits.fake.FakeHabitRepository
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

class HomeScreenViewModelTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    private val date = "2023-04-15"
    private val time = "T10:30:45Z"

    private lateinit var fakeRepository: FakeHabitRepository
    private lateinit var homeViewModel: HomeScreenViewModel

    @Before
    fun createViewModel() {
        fakeRepository = FakeHabitRepository()
        homeViewModel = HomeScreenViewModel(
            habitsRepository = fakeRepository,
            clock = Clock.fixed(Instant.parse(date + time), Clock.systemDefaultZone().zone)
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun viewModelHomeUiState_verifyHomeUiStateInitialEmptyValue() = runTest {

        val expectedHomeUiState = HomeUiState()
        val job = launch(UnconfinedTestDispatcher()) {
            homeViewModel.homeUiState.collect()
        }

        assertEquals(expectedHomeUiState, homeViewModel.homeUiState.value)

        job.cancel()

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun viewModelHomeUiState_verifyHomeUiStateInitialSetValue() = runTest {

        val expectedHomeUiState = HomeUiState(
            listOf(
                HomeHabitUiState(habit1.id, habit1.name, true),
                HomeHabitUiState(habit2.id, habit2.name, false)
            )
        )

        addTwoHabit()
        fakeRepository.toggleEntry(habit1.id, date)

        val job = launch(UnconfinedTestDispatcher()) {
            homeViewModel.homeUiState.collect()
        }

        assertEquals(expectedHomeUiState, homeViewModel.homeUiState.value)

        job.cancel()

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun viewModelHomeUiState_verifyNewHabitsDisplayed() = runTest {

        val expectedHomeUiState = HomeUiState(
            listOf(
                HomeHabitUiState(habit1.id, habit1.name, false)
            )
        )

        val job = launch(UnconfinedTestDispatcher()) {
            homeViewModel.homeUiState.collect()
        }

        addOneHabit()
        assertEquals(expectedHomeUiState, homeViewModel.homeUiState.value)

        job.cancel()

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun viewModelHomeUiState_verifyCompletedDisplayed() = runTest {

        val expectedHomeUiState = HomeUiState(
            listOf(
                HomeHabitUiState(habit1.id, habit1.name, false),
                HomeHabitUiState(habit2.id, habit2.name, true)
            )
        )

        val job = launch(UnconfinedTestDispatcher()) {
            homeViewModel.homeUiState.collect()
        }

        addTwoHabit()
        homeViewModel.toggleEntry(habit2.id)
        assertEquals(expectedHomeUiState, homeViewModel.homeUiState.value)

        job.cancel()

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun viewModelHomeUiState_verifyUncompletedDisplayed() = runTest {

        addOneHabit()
        fakeRepository.toggleEntry(habit1.id, date)

        val expectedHomeUiState = HomeUiState(
            listOf(
                HomeHabitUiState(habit1.id, habit1.name, false)
            )
        )

        val job = launch(UnconfinedTestDispatcher()) {
            homeViewModel.homeUiState.collect()
        }

        homeViewModel.toggleEntry(habit1.id)
        assertEquals(expectedHomeUiState, homeViewModel.homeUiState.value)

        job.cancel()

    }

    private suspend fun addOneHabit() {
        fakeRepository.addHabit(habit1)
    }

    private suspend fun addTwoHabit() {
        fakeRepository.addHabit(habit1)
        fakeRepository.addHabit(habit2)
    }

}