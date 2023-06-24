package com.willbsp.habits.ui

import androidx.activity.ComponentActivity
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.willbsp.habits.HiltComponentActivity
import com.willbsp.habits.R
import com.willbsp.habits.data.TestData.habit1
import com.willbsp.habits.data.TestData.habit2
import com.willbsp.habits.data.repository.EntryRepository
import com.willbsp.habits.data.repository.HabitRepository
import com.willbsp.habits.domain.usecase.GetHabitsWithVirtualEntriesUseCase
import com.willbsp.habits.domain.usecase.GetVirtualEntriesUseCase
import com.willbsp.habits.helper.onNodeWithTextId
import com.willbsp.habits.ui.screens.logbook.LogbookScreen
import com.willbsp.habits.ui.screens.logbook.LogbookViewModel
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
import java.time.Month
import java.time.ZoneOffset
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class LogbookScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()

    private lateinit var activity: ComponentActivity

    @Inject
    lateinit var habitRepository: HabitRepository

    @Inject
    lateinit var entryRepository: EntryRepository

    private val date = LocalDate.parse("2023-03-10")
    private val time = "T12:00:00Z"

    @Before
    fun setup() {
        hiltRule.inject()
        val getVEntries = GetVirtualEntriesUseCase(habitRepository, entryRepository)
        val getHabitsWithVEntries = GetHabitsWithVirtualEntriesUseCase(habitRepository, getVEntries)
        val clock = Clock.fixed(Instant.parse(date.toString() + time), ZoneOffset.UTC)
        composeTestRule.setContent {
            val viewModel = LogbookViewModel(
                habitRepository = habitRepository,
                entryRepository = entryRepository,
                getVirtualEntries = getHabitsWithVEntries,
                clock = clock
            )
            Surface {
                val state by viewModel.uiState.collectAsStateWithLifecycle()
                LogbookScreen(
                    navigateUp = { },
                    logbookUiState = state,
                    completedOnClick = { viewModel.toggleEntry(it) },
                    habitOnClick = { viewModel.setSelectedHabit(it) }
                )
            }
            activity = composeTestRule.activity
        }
    }

    @Test
    fun noHabits_noHabitsScreen() {
        composeTestRule.onNodeWithTextId(R.string.logbook_empty_text).assertExists()
    }

    @Test
    fun habitsExist_habitsShown() = runTest {
        habitRepository.upsertHabit(habit1)
        habitRepository.upsertHabit(habit2)
        composeTestRule.onNodeWithText(habit1.name).assertExists()
        composeTestRule.onNodeWithText(habit2.name).assertExists()
    }

    @Test
    fun calendarMonths_areCorrect() = runTest {
        habitRepository.upsertHabit(habit1)
        var day = date
        composeTestRule.onNodeWithText(
            day.month.getDisplayName(TextStyle.FULL, Locale.getDefault()), substring = true
        ).assertExists()
        repeat(6) {
            previousMonth(day)
            day = day.minusMonths(1)
            composeTestRule.onNodeWithText(
                day.month
                    .getDisplayName(TextStyle.FULL, Locale.getDefault()), substring = true
            ).assertExists()
        }
    }

    @Test
    fun calendarDays_areCorrect() = runTest {
        habitRepository.upsertHabit(habit1)
        var day = date
        getButtonForDate(LocalDate.of(day.year, day.month, 1)).assertExists()
        getButtonForDate(LocalDate.of(day.year, day.month, day.month.maxLength())).assertExists()

        previousMonth(day)
        day = day.minusMonths(1)
        getButtonForDate(LocalDate.of(day.year, day.month, 1))
            .assertExists()
        getButtonForDate(LocalDate.of(day.year, day.month, 28)) // feb
            .assertExists()

        repeat(6) {
            previousMonth(day)
            day = day.minusMonths(1)
            getButtonForDate(LocalDate.of(day.year, day.month, 1))
                .assertExists()
            getButtonForDate(LocalDate.of(day.year, day.month, day.month.maxLength()))
                .assertExists()
        }

    }

    private fun nextMonth(currentMonth: Month) {
        composeTestRule.onNodeWithTag(
            activity.getString(R.string.logbook_previous_month) +
                    " " +
                    currentMonth.getDisplayName(TextStyle.FULL, Locale.getDefault()) +
                    " " +
                    date.year
        ).performClick()
    }

    private fun previousMonth(currentMonth: LocalDate) {
        composeTestRule.onNodeWithTag(
            activity.getString(R.string.logbook_previous_month) +
                    " " +
                    currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault()) +
                    " " +
                    currentMonth.year
        ).performClick()
    }

    private fun getButtonForDate(date: LocalDate): SemanticsNodeInteraction {
        return composeTestRule.onNodeWithTag(date.toString())
    }

}