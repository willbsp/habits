package com.willbsp.habits.ui

import androidx.activity.ComponentActivity
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onSiblings
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.willbsp.habits.HiltComponentActivity
import com.willbsp.habits.R
import com.willbsp.habits.data.TestData.habit1
import com.willbsp.habits.data.TestData.habit2
import com.willbsp.habits.data.TestData.habit3
import com.willbsp.habits.data.TestData.habit4
import com.willbsp.habits.data.TestData.reminder1
import com.willbsp.habits.data.TestData.reminder4
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.model.Reminder
import com.willbsp.habits.data.repository.EntryRepository
import com.willbsp.habits.data.repository.HabitRepository
import com.willbsp.habits.data.repository.ReminderRepository
import com.willbsp.habits.domain.usecase.CalculateScoreUseCase
import com.willbsp.habits.domain.usecase.CalculateStatisticsUseCase
import com.willbsp.habits.domain.usecase.CalculateStreakUseCase
import com.willbsp.habits.domain.usecase.GetVirtualEntriesUseCase
import com.willbsp.habits.fake.repository.FakeEntryRepository
import com.willbsp.habits.helper.onNodeWithTextId
import com.willbsp.habits.ui.screens.detail.DetailScreen
import com.willbsp.habits.ui.screens.detail.DetailViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Clock
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class DetailScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()

    @Inject
    lateinit var habitRepository: HabitRepository

    @Inject
    lateinit var entryRepository: EntryRepository

    @Inject
    lateinit var reminderRepository: ReminderRepository

    private lateinit var activity: ComponentActivity

    private lateinit var viewModel: DetailViewModel
    private lateinit var clock: Clock
    private lateinit var getVirtualEntriesUseCase: GetVirtualEntriesUseCase
    private lateinit var calculateScoreUseCase: CalculateScoreUseCase
    private lateinit var calculateStreakUseCase: CalculateStreakUseCase
    private lateinit var calculateStatisticsUseCase: CalculateStatisticsUseCase

    private val date = LocalDate.parse("2023-03-10")
    private val time = "T12:00:00Z"

    @Before
    fun setup() {
        hiltRule.inject()
        runBlocking { habitRepository.upsertHabit(habit1) }
        clock = Clock.fixed(Instant.parse(date.toString() + time), ZoneOffset.UTC)
        getVirtualEntriesUseCase = GetVirtualEntriesUseCase(habitRepository, entryRepository)
        calculateScoreUseCase = CalculateScoreUseCase(getVirtualEntriesUseCase, clock)
        calculateStreakUseCase = CalculateStreakUseCase(getVirtualEntriesUseCase, clock)
        calculateStatisticsUseCase = CalculateStatisticsUseCase(entryRepository)
    }

    private fun init(habit: Habit) {
        runBlocking { habitRepository.upsertHabit(habit) }
        composeTestRule.setContent {
            viewModel = DetailViewModel(
                habitRepository = habitRepository,
                reminderRepository = reminderRepository,
                savedStateHandle = SavedStateHandle(mapOf(Pair("habitId", habit.id))),
                calculateScoreUseCase = calculateScoreUseCase,
                calculateStreakUseCase = calculateStreakUseCase,
                calculateStatisticsUseCase = calculateStatisticsUseCase,
                clock = clock
            )
            Surface {
                val state by viewModel.uiState.collectAsStateWithLifecycle()
                DetailScreen(
                    detailUiState = state,
                    navigateUp = { },
                    navigateToEditHabit = { }
                )
            }
        }
        activity = composeTestRule.activity
    }

    @Test
    fun dailyHabit_showsCorrectFrequency() {
        init(habit1)
        composeTestRule.onNodeWithTextId(R.string.detail_daily).assertExists()
    }

    @Test
    fun weeklyHabitOnceAWeek_showsCorrectFrequency() {
        init(habit2)
        composeTestRule.onNodeWithText(
            activity.resources.getQuantityString(R.plurals.detail_times_per_week, 1, 1)
        ).assertExists()
    }

    @Test
    fun weeklyHabitMultipleTimesWeek_showsCorrectFrequency() {
        init(habit4)
        composeTestRule.onNodeWithText(
            activity.resources.getQuantityString(R.plurals.detail_times_per_week, 3, 3)
        ).assertExists()
    }

    @Test
    fun dailyHabit_showsCorrectScore() = runTest {
        init(habit3)
        (entryRepository as FakeEntryRepository).populate()
        composeTestRule.onNodeWithText("37%").assertExists()
    }

    @Test
    fun weeklyHabit_showsCorrectScore() = runTest {
        init(habit4)
        (entryRepository as FakeEntryRepository).populate2()
        composeTestRule.onNodeWithText("24%").assertExists()
    }

    @Test
    fun dailyHabit_showsCorrectStreak() = runTest {
        init(habit3)
        (entryRepository as FakeEntryRepository).populate()
        composeTestRule.onNodeWithTextId(R.string.detail_streak)
            .onSiblings().filterToOne(hasText("5")).assertExists()
    }

    @Test
    fun weeklyHabit_showsCorrectStreak() = runTest {
        init(habit4)
        (entryRepository as FakeEntryRepository).populate2()
        composeTestRule.onNodeWithTextId(R.string.detail_streak)
            .onSiblings().filterToOne(hasText("0")).assertExists()
    }

    @Test
    fun dailyHabit_showsLongestStreak() = runTest {
        init(habit3)
        (entryRepository as FakeEntryRepository).populate()
        composeTestRule.onNodeWithTextId(R.string.detail_longest_streak)
            .onSiblings().filterToOne(hasText("5")).assertExists()
    }

    @Test
    fun weeklyHabit_showsLongestStreak() = runTest {
        init(habit4)
        (entryRepository as FakeEntryRepository).populate2()
        composeTestRule.onNodeWithTextId(R.string.detail_longest_streak)
            .onSiblings().filterToOne(hasText("7")).assertExists()
    }

    @Test
    fun dailyHabit_showsCorrectTotal() = runTest {
        init(habit3)
        (entryRepository as FakeEntryRepository).populate()
        composeTestRule.onNodeWithTextId(R.string.detail_total)
            .onSiblings().filterToOne(hasText("11")).assertExists()
    }

    @Test
    fun weeklyHabit_showsCorrectTotal() = runTest {
        init(habit4)
        (entryRepository as FakeEntryRepository).populate2()
        composeTestRule.onNodeWithTextId(R.string.detail_total)
            .onSiblings().filterToOne(hasText("3")).assertExists()
    }

    @Test
    fun dailyHabit_showsCorrectStartDate() = runTest {
        init(habit3)
        val startDate = LocalDate.parse("2023-02-11")
        (entryRepository as FakeEntryRepository).populate()
        composeTestRule.onNodeWithTextId(R.string.detail_started)
            .onSiblings().filterToOne(
                hasText(
                    "${startDate.dayOfMonth} ${
                        startDate.month.getDisplayName(
                            TextStyle.SHORT,
                            Locale.getDefault()
                        )
                    }"
                )
            ).assertExists()
    }

    @Test
    fun weeklyHabit_showsCorrectStartDate() = runTest {
        init(habit4)
        val startDate = LocalDate.parse("2023-02-28")
        (entryRepository as FakeEntryRepository).populate2()
        composeTestRule.onNodeWithTextId(R.string.detail_started)
            .onSiblings().filterToOne(
                hasText(
                    "${startDate.dayOfMonth} ${
                        startDate.month.getDisplayName(
                            TextStyle.SHORT,
                            Locale.getDefault()
                        )
                    }"
                )
            ).assertExists()
    }

    @Test
    fun habitNotStarted_initialValuesShown() = runTest {
        init(habit1)
        composeTestRule.onNodeWithTextId(R.string.detail_not_started).assertExists()
        composeTestRule.onNodeWithTextId(R.string.detail_total)
            .onSiblings().filterToOne(hasText("0")).assertExists()
        composeTestRule.onNodeWithTextId(R.string.detail_streak)
            .onSiblings().filterToOne(hasText("0")).assertExists()
        composeTestRule.onNodeWithTextId(R.string.detail_longest_streak)
            .onSiblings().filterToOne(hasText("0")).assertExists()
    }

    @Test
    fun reminderEveryday_correctReminderTextShown() = runTest {
        init(habit2)
        for ((i, day) in DayOfWeek.values().withIndex()) {
            reminderRepository.insertReminder(Reminder(i, habit2.id, LocalTime.NOON, day))
        }
        composeTestRule.onNodeWithTextId(R.string.detail_every_day).assertExists()
    }

    @Test
    fun reminderOneDay_correctReminderTextShown() = runTest {
        init(habit2)
        reminderRepository.insertReminder(reminder1)
        composeTestRule.onNodeWithText(
            activity.resources.getQuantityString(R.plurals.detail_reminders, 1, 1)
        ).assertExists()
    }

    @Test
    fun reminderTwoDays_correctReminderTextShown() = runTest {
        init(habit2)
        reminderRepository.insertReminder(reminder1)
        reminderRepository.insertReminder(reminder4)
        composeTestRule.onNodeWithText(
            activity.resources.getQuantityString(R.plurals.detail_reminders, 2, 2)
        ).assertExists()
    }

}