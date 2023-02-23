package com.willbsp.habits.viewmodel

import com.willbsp.habits.TestData.habit1
import com.willbsp.habits.TestData.habit2
import com.willbsp.habits.fake.FakeHabitRepository
import com.willbsp.habits.rules.TestDispatcherRule
import com.willbsp.habits.ui.screens.home.HomeCompletedUiState
import com.willbsp.habits.ui.screens.home.HomeHabitUiState
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

class HomeViewModelTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    private val date = LocalDate.parse("2023-04-15")
    private val time = "T10:30:45Z"

    private lateinit var fakeRepository: FakeHabitRepository
    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun createViewModel() {
        fakeRepository = FakeHabitRepository()
        homeViewModel = HomeViewModel(
            habitsRepository = fakeRepository,
            clock = Clock.fixed(
                Instant.parse(date.toString() + time),
                Clock.systemDefaultZone().zone
            )
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
                HomeHabitUiState(
                    habit1.id,
                    habit1.name,
                    null,
                    listOf(HomeCompletedUiState(date, true))
                ),
                HomeHabitUiState(habit2.id, habit2.name, null, listOf())
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
                HomeHabitUiState(habit1.id, habit1.name, null, listOf())
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
                HomeHabitUiState(habit1.id, habit1.name, null, listOf()),
                HomeHabitUiState(
                    habit2.id,
                    habit2.name,
                    null,
                    listOf(HomeCompletedUiState(date, true))
                )
            )
        )

        val job = launch(UnconfinedTestDispatcher()) {
            homeViewModel.homeUiState.collect()
        }

        addTwoHabit()
        homeViewModel.toggleEntry(habit2.id, date)
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
                HomeHabitUiState(habit1.id, habit1.name, null, listOf())
            )
        )

        val job = launch(UnconfinedTestDispatcher()) {
            homeViewModel.homeUiState.collect()
        }

        homeViewModel.toggleEntry(habit1.id, date)
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