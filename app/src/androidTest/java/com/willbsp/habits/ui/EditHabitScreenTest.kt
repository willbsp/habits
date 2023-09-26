package com.willbsp.habits.ui

import androidx.compose.material3.Surface
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.willbsp.habits.HiltComponentActivity
import com.willbsp.habits.R
import com.willbsp.habits.data.TestData.habit1
import com.willbsp.habits.data.TestData.habit2
import com.willbsp.habits.data.TestData.reminder5
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.model.HabitFrequency
import com.willbsp.habits.data.repository.HabitRepository
import com.willbsp.habits.data.repository.ReminderRepository
import com.willbsp.habits.domain.usecase.SaveHabitUseCase
import com.willbsp.habits.fake.util.FakeReminderManager
import com.willbsp.habits.helper.onNodeWithContentDescriptionId
import com.willbsp.habits.helper.onNodeWithTextId
import com.willbsp.habits.ui.common.form.HabitFormUiState
import com.willbsp.habits.ui.screens.edit.EditScreen
import com.willbsp.habits.ui.screens.edit.EditViewModel
import com.willbsp.habits.util.ReminderManager
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class EditHabitScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()

    @Inject
    lateinit var habitRepository: HabitRepository

    @Inject
    lateinit var reminderRepository: ReminderRepository

    @Before
    fun setup() {
        hiltRule.inject()
        val reminderManager: ReminderManager = FakeReminderManager()
        runBlocking {
            habitRepository.upsertHabit(habit1)
            reminderRepository.insertReminder(reminder5)
        }
        composeTestRule.setContent {
            val viewModel = EditViewModel(
                habitRepository = habitRepository,
                reminderRepository = reminderRepository,
                reminderManager = reminderManager,
                saveHabit = SaveHabitUseCase(
                    habitRepository,
                    reminderRepository,
                    reminderManager
                ),
                savedStateHandle = SavedStateHandle(mapOf(Pair("habitId", habit1.id)))
            )
            Surface {
                val state = viewModel.uiState
                EditScreen(
                    navigateUp = {},
                    onSaveClick = { viewModel.saveHabit() },
                    onValueChange = { uiState -> viewModel.updateUiState(uiState as HabitFormUiState.Data) },
                    onDeleteClick = { viewModel.deleteHabit() },
                    formUiState = state,
                )
            }
        }
    }

    @Test
    fun emptyHabit_noChange() = runTest {
        val initial = habitRepository.getAllHabitsStream().first()
        composeTestRule.onNodeWithTextId(R.string.modify_habit_name)
            .performClick()
            .performTextClearance()
        composeTestRule.onNodeWithContentDescriptionId(R.string.edit_habit_update_habit)
            .performClick()
        composeTestRule.onNodeWithTextId(R.string.modify_habit_name).assertExists()
        val habits = habitRepository.getAllHabitsStream().first()
        assertEquals(initial, habits)
    }

    @Test
    fun habitNameTooShort_noChange() = runTest {
        val initial = habitRepository.getAllHabitsStream().first()
        composeTestRule.onNodeWithTextId(R.string.modify_habit_name)
            .performClick()
            .performTextClearance()
        composeTestRule.onNodeWithTextId(R.string.modify_habit_name)
            .performClick()
            .performTextInput("")
        composeTestRule.onNodeWithContentDescriptionId(R.string.edit_habit_update_habit)
            .performClick()
        composeTestRule.onNodeWithTextId(R.string.modify_habit_name).assertExists()
        val habits = habitRepository.getAllHabitsStream().first()
        assertEquals(initial, habits)
    }

    @Test
    fun habitNameTooLong_noChange() = runTest {
        val initial = habitRepository.getAllHabitsStream().first()
        composeTestRule.onNodeWithTextId(R.string.modify_habit_name)
            .performClick()
            .performTextClearance()
        composeTestRule.onNodeWithTextId(R.string.modify_habit_name)
            .performClick()
            .performTextInput("this name is too long to be a habit name")
        composeTestRule.onNodeWithContentDescriptionId(R.string.edit_habit_update_habit)
            .performClick()
        composeTestRule.onNodeWithTextId(R.string.modify_habit_name).assertExists()
        val habits = habitRepository.getAllHabitsStream().first()
        assertEquals(initial, habits)
    }

    @Test
    fun validDailyHabit_isUpdated() = runTest {
        val nameField = composeTestRule.onNodeWithTextId(R.string.modify_habit_name)
        nameField.performClick()
        nameField.performTextClearance()
        nameField.performTextInput(habit2.name)
        composeTestRule.onNodeWithContentDescriptionId(R.string.edit_habit_update_habit)
            .performClick()
        val habits = habitRepository.getAllHabitsStream().first()
        assertEquals(1, habits.size)
        assertEquals(habit2.name, habits.first().name)
        assertEquals(habit1.frequency, habits.first().frequency)
    }

    @Test
    fun validWeeklyHabit_isUpdated() = runTest {
        composeTestRule.onNodeWithTextId(R.string.modify_frequency).performClick()
        composeTestRule.onNodeWithTextId(R.string.frequency_weekly).performClick()
        composeTestRule.onNodeWithTextId(R.string.modify_times_per_week).performClick()
        composeTestRule.onNodeWithText("3").performClick()
        composeTestRule.onNodeWithContentDescriptionId(R.string.edit_habit_update_habit)
            .performClick()
        val habits = habitRepository.getAllHabitsStream().first()
        assertEquals(1, habits.size)
        assertEquals(habit1.name, habits.first().name)
        assertEquals(HabitFrequency.WEEKLY, habits.first().frequency)
        assertEquals(3, habits.first().repeat)
    }

    @Test
    fun deleteHabit_isDeleted() = runTest {
        assertEquals(1, habitRepository.getAllHabitsStream().first().size)
        composeTestRule.onNodeWithTextId(R.string.edit_habit_delete).performClick()
        composeTestRule.onNodeWithTextId(R.string.edit_confirm).performClick()
        assertEquals(emptyList<Habit>(), habitRepository.getAllHabitsStream().first())
    }

    @Test
    fun reminderExists_isLoadedCorrectly() = runTest {
        composeTestRule.onNodeWithTextId(R.string.modify_reminder_specific_days).assertExists()
        composeTestRule.onNodeWithText(
            reminder5.day.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        ).assertExists()
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
        composeTestRule.onNodeWithTextId(R.string.modify_reminder).performClick()
        composeTestRule.onNodeWithTextId(R.string.modify_reminder_specific_days).performClick()
        composeTestRule.onNodeWithTextId(R.string.modify_reminder_time).assertExists()
        composeTestRule.onNodeWithTextId(R.string.modify_reminder_days).assertExists()
    }

    @Test
    fun noReminder_reminderIsDeleted() = runTest {
        composeTestRule.onNodeWithTextId(R.string.modify_reminder).performClick()
        composeTestRule.onNodeWithTextId(R.string.modify_reminder_none).performClick()
        composeTestRule.onNodeWithContentDescriptionId(R.string.edit_habit_update_habit)
            .performClick()
        val reminders = reminderRepository.getAllRemindersStream().first()
        assertTrue(reminders.isEmpty())
    }

}















