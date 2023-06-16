package com.willbsp.habits.ui

import androidx.activity.ComponentActivity
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.willbsp.habits.HiltComponentActivity
import com.willbsp.habits.R
import com.willbsp.habits.data.TestData.habit1
import com.willbsp.habits.data.TestData.habit2
import com.willbsp.habits.data.TestData.habit3
import com.willbsp.habits.data.TestData.habit4
import com.willbsp.habits.data.repository.EntryRepository
import com.willbsp.habits.data.repository.HabitRepository
import com.willbsp.habits.data.repository.SettingsRepository
import com.willbsp.habits.domain.usecase.CalculateStreakUseCase
import com.willbsp.habits.domain.usecase.GetHabitsWithVirtualEntriesUseCase
import com.willbsp.habits.domain.usecase.GetVirtualEntriesUseCase
import com.willbsp.habits.fake.repository.FakeEntryRepository
import com.willbsp.habits.helper.hasContentDescriptionId
import com.willbsp.habits.helper.onNodeWithContentDescriptionId
import com.willbsp.habits.helper.onNodeWithTextId
import com.willbsp.habits.ui.screens.home.HomeScreen
import com.willbsp.habits.ui.screens.home.HomeViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class HomeScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()

    private lateinit var activity: ComponentActivity

    private val date = LocalDate.parse("2023-03-10")
    private val time = "T12:00:00Z"

    @Inject
    lateinit var entryRepository: EntryRepository

    @Inject
    lateinit var habitRepository: HabitRepository

    @Inject
    lateinit var virtualEntriesUseCase: GetVirtualEntriesUseCase

    @Inject
    lateinit var settingsRepository: SettingsRepository

    @Before
    fun setup() {
        hiltRule.inject()
        val clock = Clock.fixed(Instant.parse(date.toString() + time), ZoneOffset.UTC)
        composeTestRule.setContent {
            val getHabitsWithVirtualEntriesUseCase =
                GetHabitsWithVirtualEntriesUseCase(habitRepository, virtualEntriesUseCase)
            val viewModel = HomeViewModel(
                entryRepository = entryRepository,
                settingsRepository = settingsRepository,
                getHabitsWithVirtualEntries = getHabitsWithVirtualEntriesUseCase,
                calculateStreak = CalculateStreakUseCase(virtualEntriesUseCase, clock),
                clock = clock
            )
            Surface {
                val state by viewModel.uiState.collectAsStateWithLifecycle()
                HomeScreen(
                    completedOnClick = { id, date -> viewModel.toggleEntry(id, date) },
                    navigateToLogbook = { },
                    navigateToAddHabit = { },
                    navigateToDetail = { },
                    navigateToSettings = { },
                    homeUiState = state
                )
            }
        }
        activity = composeTestRule.activity
    }

    @Test
    fun noHabits_noHabitsTooltipShown() {
        composeTestRule.onNodeWithTextId(R.string.home_no_habits).assertExists()
    }

    @Test
    fun habitExists_showHabitInList() = runTest {
        habitRepository.upsertHabit(habit1)
        composeTestRule.onNodeWithText(habit1.name).assertExists()
    }

    @Test
    fun habitExistsDaily_showTodayTitle() = runTest {
        habitRepository.upsertHabit(habit1)
        composeTestRule.onNodeWithTextId(R.string.home_today).assertExists()
    }

    @Test
    fun habitExistsWeekly_showWeekTitle() = runTest {
        habitRepository.upsertHabit(habit2)
        composeTestRule.onNodeWithTextId(R.string.home_this_week).assertExists()
    }

    @Test
    fun dailyHabit_toggleCompleted_habitDisappears() = runTest {
        habitRepository.upsertHabit(habit1)
        composeTestRule.onNodeWithText(habit1.name).assertExists()
        val habitCard = composeTestRule.onNodeWithText(habit1.name)
        habitCard.onChildren()
            .filterToOne(hasContentDescriptionId(R.string.home_completed, activity))
            .performClick()
        composeTestRule.onNodeWithText(habit1.name).assertDoesNotExist()
        composeTestRule.onNodeWithTextId(R.string.home_all_completed).assertExists()
    }

    @Test
    fun dailyHabit_toggleShowCompleted_showsCompletedHabit() = runTest {
        habitRepository.upsertHabit(habit1)
        composeTestRule.onNodeWithText(habit1.name).assertExists()
        val habitCard = composeTestRule.onNodeWithText(habit1.name)
        habitCard.onChildren()
            .filterToOne(hasContentDescriptionId(R.string.home_completed, activity))
            .performClick()
        composeTestRule.onNodeWithText(habit1.name).assertDoesNotExist()
        composeTestRule.onNodeWithContentDescriptionId(R.string.home_show_completed).performClick()
        composeTestRule.onNodeWithText(habit1.name).assertExists()
    }

    @Test
    fun dailyHabit_streakShownAndCorrect() = runTest {
        habitRepository.upsertHabit(habit3)
        (entryRepository as FakeEntryRepository).populate()
        composeTestRule.onNodeWithText("5").assertExists()
    }

    @Test
    fun weeklyHabit_streakShownAndCorrect() = runTest {
        habitRepository.upsertHabit(habit4)
        entryRepository.toggleEntry(habit4.id, LocalDate.parse("2023-03-06"))
        entryRepository.toggleEntry(habit4.id, LocalDate.parse("2023-03-07"))
        entryRepository.toggleEntry(habit4.id, LocalDate.parse("2023-03-08"))
        composeTestRule.onNodeWithContentDescriptionId(R.string.home_show_completed).performClick()
        composeTestRule.onNodeWithText("5").assertExists()
    }

}
