package com.willbsp.habits.ui

import androidx.activity.ComponentActivity
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasContentDescription
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
import com.willbsp.habits.data.model.Entry
import com.willbsp.habits.data.repository.EntryRepository
import com.willbsp.habits.data.repository.HabitRepository
import com.willbsp.habits.data.repository.SettingsRepository
import com.willbsp.habits.domain.usecase.CalculateScoreUseCase
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
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
        val snackbarHostState = SnackbarHostState()
        composeTestRule.setContent {
            val getHabitsWithVirtualEntriesUseCase =
                GetHabitsWithVirtualEntriesUseCase(habitRepository, virtualEntriesUseCase)
            val viewModel = HomeViewModel(
                entryRepository = entryRepository,
                settingsRepository = settingsRepository,
                getHabitsWithVirtualEntries = getHabitsWithVirtualEntriesUseCase,
                calculateStreak = CalculateStreakUseCase(virtualEntriesUseCase, clock),
                calculateScore = CalculateScoreUseCase(virtualEntriesUseCase, clock),
                clock = clock
            )
            Surface {
                val state by viewModel.uiState.collectAsStateWithLifecycle()
                HomeScreen(
                    snackbarHostState = snackbarHostState,
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
    fun toggleButton_whenEntryExists_isOn() = runTest {
        composeTestRule.onNodeWithContentDescriptionId(R.string.home_show_completed).performClick()
        habitRepository.upsertHabit(habit1)
        entryRepository.toggleEntry(habit1.id, date)
        entryRepository.toggleEntry(habit1.id, date.minusDays(1))
        entryRepository.toggleEntry(habit1.id, date.minusDays(2))
        entryRepository.toggleEntry(habit1.id, date.minusDays(4))
        getDateToggle(habit1.name).assertIsOn()
        composeTestRule.onNodeWithText(habit1.name).performClick()
        getDateToggle(habit1.name, date.minusDays(1)).assertIsOn()
        getDateToggle(habit1.name, date.minusDays(2)).assertIsOn()
        getDateToggle(habit1.name, date.minusDays(4)).assertIsOn()
    }

    @Test
    fun clickHabit_showsPreviousDaysAndDetail() = runTest {
        habitRepository.upsertHabit(habit1)
        composeTestRule.onNodeWithText(habit1.name).performClick()
        getDateToggle(habit1.name, date.minusDays(1)).assertExists()
        getDateToggle(habit1.name, date.minusDays(2)).assertExists()
        getDateToggle(habit1.name, date.minusDays(3)).assertExists()
        composeTestRule.onNodeWithText(habit1.name)
            .onChildren()
            .filterToOne(hasContentDescriptionId(R.string.home_detail, activity))
            .assertExists()
    }

    @Test
    fun dailyHabit_toggleCompleted_habitDisappears() = runTest {
        habitRepository.upsertHabit(habit1)
        composeTestRule.onNodeWithText(habit1.name).assertExists()
        getDateToggle(habit1.name).performClick()
        composeTestRule.onNodeWithText(habit1.name).assertDoesNotExist()
        composeTestRule.onNodeWithTextId(R.string.home_all_completed).assertExists()
    }

    @Test
    fun dailyHabit_toggleShowCompleted_showsCompletedHabit() = runTest {
        habitRepository.upsertHabit(habit1)
        composeTestRule.onNodeWithText(habit1.name).assertExists()
        getDateToggle(habit1.name).performClick()
        composeTestRule.onNodeWithText(habit1.name).assertDoesNotExist()
        composeTestRule.onNodeWithContentDescriptionId(R.string.home_show_completed).performClick()
        composeTestRule.onNodeWithText(habit1.name).assertExists()
    }

    @Test
    fun weeklyHabit_toggleShowCompleted_showsCompletedHabit() = runTest {
        habitRepository.upsertHabit(habit4)
        composeTestRule.onNodeWithText(habit4.name).assertExists()
        composeTestRule.onNodeWithText(habit4.name).performClick()
        getDateToggle(habit4.name, date.minusDays(1)).performClick()
        getDateToggle(habit4.name, date.minusDays(2)).performClick()
        getDateToggle(habit4.name, date.minusDays(3)).performClick()
        composeTestRule.onNodeWithText(habit4.name).assertDoesNotExist()
        composeTestRule.onNodeWithContentDescriptionId(R.string.home_show_completed).performClick()
        composeTestRule.onNodeWithText(habit4.name).assertExists()
    }

    @Test
    fun weeklyHabit_toggleCompletedToday_habitDisappears() = runTest {
        habitRepository.upsertHabit(habit4)
        composeTestRule.onNodeWithText(habit4.name).assertExists()
        getDateToggle(habit4.name).performClick()
        composeTestRule.onNodeWithText(habit4.name).assertDoesNotExist()
        composeTestRule.onNodeWithTextId(R.string.home_all_completed).assertExists()
    }

    @Test
    fun weeklyHabit_toggleCompletedCompletesWeek_habitDisappears() = runTest {
        habitRepository.upsertHabit(habit4)
        composeTestRule.onNodeWithText(habit4.name).assertExists()
        composeTestRule.onNodeWithText(habit4.name).performClick()
        getDateToggle(habit4.name, date.minusDays(1)).performClick()
        getDateToggle(habit4.name, date.minusDays(2)).performClick()
        getDateToggle(habit4.name, date.minusDays(3)).performClick()
        composeTestRule.onNodeWithText(habit4.name).assertDoesNotExist()
        composeTestRule.onNodeWithTextId(R.string.home_all_completed).assertExists()
    }

    @Test
    fun dailyHabit_streakShownAndCorrect() = runTest {
        settingsRepository.saveStatisticPreference(true)
        habitRepository.upsertHabit(habit3)
        (entryRepository as FakeEntryRepository).populate()
        composeTestRule.onNodeWithText("5").assertExists()
    }

    @Test
    fun showStatisticDisabled_doesNotShowStreak() = runTest {
        settingsRepository.saveStatisticPreference(false)
        habitRepository.upsertHabit(habit3)
        (entryRepository as FakeEntryRepository).populate()
        composeTestRule.onNodeWithText("5").assertDoesNotExist()
    }

    @Test
    fun showScoresEnabled_showsScoreAndCorrect() = runTest {
        settingsRepository.saveScorePreference(true)
        habitRepository.upsertHabit(habit3)
        (entryRepository as FakeEntryRepository).populate()
        composeTestRule.onNodeWithText("37%").assertExists()
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

    @Test
    fun showSubtitleEnabled_showsSubtitle() = runTest {
        settingsRepository.saveSubtitlePreference(true)
        habitRepository.upsertHabit(habit1)
        habitRepository.upsertHabit(habit4)
        val dateText = "${date.dayOfWeek} ${date.dayOfMonth}"
        composeTestRule.onNodeWithText(habit1.name)
            .onChildren()
            .filterToOne(hasContentDescription(dateText))
            .performClick()
        composeTestRule.onNodeWithText(
            activity.resources.getQuantityString(R.plurals.home_habit_list_subtitle, 1, 1)
        ).assertExists()
    }


    @Test
    fun showSubtitleEnabled_showsCorrectSubtitleCount() = runTest {
        settingsRepository.saveSubtitlePreference(true)
        habitRepository.upsertHabit(habit1)
        habitRepository.upsertHabit(habit2)
        habitRepository.upsertHabit(habit3)
        habitRepository.upsertHabit(habit4)
        val dateText = "${date.dayOfWeek} ${date.dayOfMonth}"
        composeTestRule.onNodeWithText(habit1.name)
            .onChildren()
            .filterToOne(hasContentDescription(dateText))
            .performClick()
        composeTestRule.onNodeWithText(habit2.name).performClick()
        getDateToggle(habit2.name, date.minusDays(1)).performClick()
        composeTestRule.onNodeWithText(habit3.name)
            .onChildren()
            .filterToOne(hasContentDescription(dateText))
            .performClick()
        composeTestRule.onNodeWithText(
            activity.resources.getQuantityString(R.plurals.home_habit_list_subtitle, 3, 3)
        ).assertExists()
    }

    @Test
    fun showSubtitleDisabled_doesNotShowSubtitle() = runTest {
        settingsRepository.saveSubtitlePreference(false)
        habitRepository.upsertHabit(habit1)
        habitRepository.upsertHabit(habit4)
        val dateText = "${date.dayOfWeek} ${date.dayOfMonth}"
        composeTestRule.onNodeWithText(habit1.name)
            .onChildren()
            .filterToOne(hasContentDescription(dateText))
            .performClick()
        composeTestRule.onNodeWithText("completed already", substring = true).assertDoesNotExist()
    }

    @Test
    fun toggleDate_createsEntry() = runTest {
        habitRepository.upsertHabit(habit1)
        getDateToggle(habit1.name).performClick()
        val entries = entryRepository.getAllEntriesStream(habit1.id).first()
        assertEquals(date, entries.first().date)
    }

    @Test
    fun toggleDate_removesEntry() = runTest {
        composeTestRule.onNodeWithContentDescriptionId(R.string.home_show_completed).performClick()
        habitRepository.upsertHabit(habit1)
        entryRepository.toggleEntry(habit1.id, date)
        assertEquals(date, entryRepository.getAllEntriesStream(habit1.id).first().first().date)
        getDateToggle(habit1.name).performClick()
        assertEquals(emptyList<Entry>(), entryRepository.getAllEntriesStream(habit1.id).first())
    }

    @Test
    fun toggleDateDuringWeek_createsEntry() = runTest {
        habitRepository.upsertHabit(habit1)
        composeTestRule.onNodeWithText(habit1.name).performClick()
        getDateToggle(habit1.name, date.minusDays(2)).performClick()
        val entries = entryRepository.getAllEntriesStream(habit1.id).first()
        assertEquals(date.minusDays(2), entries.first().date)
    }

    @Test
    fun toggleDateDuringWeek_removesEntry() = runTest {
        habitRepository.upsertHabit(habit1)
        entryRepository.toggleEntry(habit1.id, date.minusDays(2))
        val entries = entryRepository.getAllEntriesStream(habit1.id).first()
        assertEquals(date.minusDays(2), entries.first().date)
        composeTestRule.onNodeWithText(habit1.name).performClick()
        getDateToggle(habit1.name, date.minusDays(2)).performClick()
        assertEquals(emptyList<Entry>(), entryRepository.getAllEntriesStream(habit1.id).first())
    }

    private fun getDateToggle(
        habitName: String,
        date: LocalDate = this.date
    ): SemanticsNodeInteraction {
        val habitCard = composeTestRule.onNodeWithText(habitName)
        val dateText = "${date.dayOfWeek} ${date.dayOfMonth}"
        return habitCard.onChildren().filterToOne(hasContentDescription(dateText))
    }

}
