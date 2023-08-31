package com.willbsp.habits.ui

import androidx.compose.material3.Surface
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.willbsp.habits.HiltComponentActivity
import com.willbsp.habits.R
import com.willbsp.habits.data.TestData.habit1
import com.willbsp.habits.data.model.HabitFrequency
import com.willbsp.habits.data.repository.HabitRepository
import com.willbsp.habits.data.repository.ReminderRepository
import com.willbsp.habits.domain.usecase.SaveHabitUseCase
import com.willbsp.habits.fake.util.FakeReminderManager
import com.willbsp.habits.helper.onNodeWithContentDescriptionId
import com.willbsp.habits.helper.onNodeWithTextId
import com.willbsp.habits.ui.screens.add.AddScreen
import com.willbsp.habits.ui.screens.add.AddViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class AddHabitScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()

    @get:Rule(order = 2)
    val notificationsPermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(android.Manifest.permission.POST_NOTIFICATIONS)

    @Inject
    lateinit var habitRepository: HabitRepository

    @Inject
    lateinit var reminderRepository: ReminderRepository

    @Before
    fun setup() {
        hiltRule.inject()
        composeTestRule.setContent {
            val saveHabitUseCase =
                SaveHabitUseCase(
                    habitRepository,
                    reminderRepository,
                    FakeReminderManager()
                )
            val viewModel = AddViewModel(saveHabitUseCase)
            Surface {
                val state = viewModel.uiState
                AddScreen(
                    navigateUp = {},
                    onSaveClick = { viewModel.saveHabit() },
                    onValueChange = { uiState -> viewModel.updateUiState(uiState) },
                    formUiState = state,
                )
            }
        }
    }

    @Test
    fun emptyHabit_isNotSaved() = runTest {
        composeTestRule.onNodeWithTextId(R.string.modify_habit_name).performTextClearance()
        composeTestRule.onNodeWithContentDescriptionId(R.string.add_habit_add_habit).performClick()
        composeTestRule.onNodeWithTextId(R.string.modify_habit_name).assertExists()
        val habits = habitRepository.getAllHabitsStream().first()
        assertEquals(0, habits.size)
    }

    @Test
    fun habitNameTooShort_isNotSaved() = runTest {
        composeTestRule.onNodeWithTextId(R.string.modify_habit_name)
            .performClick()
            .performTextInput("")
        composeTestRule.onNodeWithContentDescriptionId(R.string.add_habit_add_habit).performClick()
        composeTestRule.onNodeWithTextId(R.string.modify_habit_name).assertExists()
        val habits = habitRepository.getAllHabitsStream().first()
        assertEquals(0, habits.size)
    }

    @Test
    fun habitNameTooLong_isNotSaved() = runTest {
        composeTestRule.onNodeWithTextId(R.string.modify_habit_name)
            .performClick()
            .performTextInput("this name is too long to be a habit name")
        composeTestRule.onNodeWithContentDescriptionId(R.string.add_habit_add_habit).performClick()
        composeTestRule.onNodeWithTextId(R.string.modify_habit_name).assertExists()
        val habits = habitRepository.getAllHabitsStream().first()
        assertEquals(0, habits.size)
    }

    @Test
    fun validDailyHabit_isSaved() = runTest {
        composeTestRule.onNodeWithTextId(R.string.modify_habit_name)
            .performClick()
            .performTextInput(habit1.name)
        composeTestRule.onNodeWithContentDescriptionId(R.string.add_habit_add_habit).performClick()
        val habits = habitRepository.getAllHabitsStream().first()
        assertEquals(1, habits.size)
        assertEquals(habit1.name, habits.first().name)
        assertEquals(habit1.frequency, habits.first().frequency)
    }

    @Test
    fun validWeeklyHabit_isSaved() = runTest {
        composeTestRule.onNodeWithTextId(R.string.modify_habit_name)
            .performClick()
            .performTextInput(habit1.name)
        composeTestRule.onNodeWithTextId(R.string.modify_frequency).performClick()
        composeTestRule.onNodeWithTextId(R.string.frequency_weekly).performClick()
        composeTestRule.onNodeWithTextId(R.string.modify_times_per_week).performClick()
        composeTestRule.onNodeWithText("3").performClick()
        composeTestRule.onNodeWithContentDescriptionId(R.string.add_habit_add_habit).performClick()
        val habits = habitRepository.getAllHabitsStream().first()
        assertEquals(1, habits.size)
        assertEquals(habit1.name, habits.first().name)
        assertEquals(HabitFrequency.WEEKLY, habits.first().frequency)
        assertEquals(3, habits.first().repeat)
    }

    @Test
    fun noReminderDefault_noTimeOrDayFieldShown() = runTest {
        composeTestRule.onNodeWithTextId(R.string.modify_reminder_time).assertDoesNotExist()
        composeTestRule.onNodeWithTextId(R.string.modify_reminder_days).assertDoesNotExist()
    }

    @Test
    fun dailyReminderSelected_timeFieldIsShown() = runTest {
        composeTestRule.onNodeWithTextId(R.string.modify_habit_name)
            .performClick()
            .performTextInput(habit1.name)
        composeTestRule.onNodeWithTextId(R.string.modify_reminder).performClick()
        composeTestRule.onNodeWithTextId(R.string.modify_reminder_every_day).performClick()
        composeTestRule.onNodeWithTextId(R.string.modify_reminder_time).assertExists()
        composeTestRule.onNodeWithTextId(R.string.modify_reminder_days).assertDoesNotExist()
    }

    @Test
    fun specificReminderSelected_timeAndDayFieldsShown() = runTest {
        composeTestRule.onNodeWithTextId(R.string.modify_habit_name)
            .performClick()
            .performTextInput(habit1.name)
        composeTestRule.onNodeWithTextId(R.string.modify_reminder).performClick()
        composeTestRule.onNodeWithTextId(R.string.modify_reminder_specific_days).performClick()
        composeTestRule.onNodeWithTextId(R.string.modify_reminder_time).assertExists()
        composeTestRule.onNodeWithTextId(R.string.modify_reminder_days).assertExists()
    }

    @Test
    fun noReminder_noReminderIsSaved() = runTest {
        composeTestRule.onNodeWithTextId(R.string.modify_habit_name)
            .performClick()
            .performTextInput(habit1.name)
        composeTestRule.onNodeWithContentDescriptionId(R.string.add_habit_add_habit).performClick()
        val reminders = reminderRepository.getAllRemindersStream().first()
        assertTrue(reminders.isEmpty())
    }

    @Test
    fun defaultDailyReminder_isSaved() = runTest {
        composeTestRule.onNodeWithTextId(R.string.modify_habit_name)
            .performClick()
            .performTextInput(habit1.name)
        composeTestRule.onNodeWithTextId(R.string.modify_reminder).performClick()
        composeTestRule.onNodeWithTextId(R.string.modify_reminder_every_day).performClick()
        composeTestRule.onNodeWithContentDescriptionId(R.string.add_habit_add_habit).performClick()
        val reminders = reminderRepository.getAllRemindersStream().first()
        assertEquals(7, reminders.count())
        for (day in DayOfWeek.values()) {
            assertTrue(reminders.any { it.day == day && it.habitId == habit1.id })
        }
    }

    @Test
    fun specificDayReminder_isSaved() = runTest {
        composeTestRule.onNodeWithTextId(R.string.modify_habit_name)
            .performClick()
            .performTextInput(habit1.name)
        composeTestRule.onNodeWithTextId(R.string.modify_reminder).performClick()
        composeTestRule.onNodeWithTextId(R.string.modify_reminder_specific_days).performClick()
        composeTestRule.onNodeWithTextId(R.string.modify_reminder_days).performClick()
        println(DayOfWeek.MONDAY.getDisplayName(TextStyle.FULL, Locale.getDefault()))
        composeTestRule.onNodeWithTag(
            DayOfWeek.MONDAY.getDisplayName(TextStyle.FULL, Locale.getDefault())
        ).performClick()
        composeTestRule.onNodeWithTag(
            DayOfWeek.THURSDAY.getDisplayName(TextStyle.FULL, Locale.getDefault())
        ).performClick()
        composeTestRule.onNodeWithTag("daypicker_confirm").performClick()
        composeTestRule.onNodeWithContentDescriptionId(R.string.add_habit_add_habit).performClick()
        val reminders = reminderRepository.getAllRemindersStream().first()
        assertEquals(2, reminders.count())
        assertTrue(reminders.any { it.day == DayOfWeek.MONDAY && it.habitId == habit1.id })
        assertTrue(reminders.any { it.day == DayOfWeek.THURSDAY && it.habitId == habit1.id })
    }


}















