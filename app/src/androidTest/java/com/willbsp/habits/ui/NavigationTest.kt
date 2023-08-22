package com.willbsp.habits.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.willbsp.habits.HiltComponentActivity
import com.willbsp.habits.R
import com.willbsp.habits.data.TestData.habit1
import com.willbsp.habits.helper.assertCurrentRouteName
import com.willbsp.habits.helper.onNodeWithContentDescriptionId
import com.willbsp.habits.helper.onNodeWithTextId
import com.willbsp.habits.ui.navigation.HabitsNavigationDestination
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()
    private lateinit var navController: NavHostController

    @Before
    fun setup() {
        hiltRule.inject()
        setupNavHost()
    }

    private fun setupNavHost() {
        composeTestRule.setContent {
            navController = rememberNavController()
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            val snackbarHostState = SnackbarHostState()
            HabitsApp(
                navController = navController,
                onDatabaseImport = {},
                snackbarState = snackbarHostState
            )
        }
    }

    // verify routes

    @Test
    fun navHost_verifyStartDestination() {
        navController.assertCurrentRouteName(HabitsNavigationDestination.HOME.route)
    }

    @Test
    fun navHost_clickAddHabit_navigatesToAddHabit() {
        navigateToAddHabitScreen()
        navController.assertCurrentRouteName(HabitsNavigationDestination.ADD.route)
    }

    @Test
    fun navHost_clickLogbook_navigatesToLogbook() {
        navigateToLogbookScreen()
        navController.assertCurrentRouteName(HabitsNavigationDestination.LOGBOOK.route)
    }

    @Test
    fun navHost_clickSettings_navigatesToSettings() {
        navigateToSettingsScreen()
        navController.assertCurrentRouteName(HabitsNavigationDestination.SETTINGS.route)
    }

    @Test
    fun navHost_clickDetail_navigatesToDetail() {
        navigateToAddHabitScreen()
        addHabit()
        navigateToDetailScreen()
        navController.assertCurrentRouteName(HabitsNavigationDestination.DETAIL.route, "habitId")
    }

    @Test
    fun navHost_clickModify_navigatesToEditHabit() {
        navigateToAddHabitScreen()
        addHabit()
        navigateToDetailScreen()
        navigateToEditHabitScreen()
        navController.assertCurrentRouteName(HabitsNavigationDestination.EDIT.route, "habitId")
    }

    @Test
    fun navHost_clickAbout_navigatesToAbout() {
        navigateToSettingsScreen()
        navigateToAboutScreen()
        navController.assertCurrentRouteName(HabitsNavigationDestination.ABOUT.route)
    }

    @Test
    fun navHost_verifyAddingHabitNavigatesBackToHome() {
        navigateToAddHabitScreen()
        addHabit()
        navController.assertCurrentRouteName(HabitsNavigationDestination.HOME.route)
    }

    @Test
    fun navHost_verifyEditingHabitNavigatesBackToDetail() {
        navigateToAddHabitScreen()
        addHabit()
        navigateToDetailScreen()
        navigateToEditHabitScreen()
        composeTestRule.onNodeWithContentDescriptionId(R.string.edit_habit_update_habit)
            .performClick()
        navController.assertCurrentRouteName(HabitsNavigationDestination.DETAIL.route, "habitId")
    }

    @Test
    fun navHost_verifyDeletingHabitNavigatesBackToHome() {
        navigateToAddHabitScreen()
        addHabit()
        navigateToDetailScreen()
        navigateToEditHabitScreen()
        composeTestRule.onNodeWithTextId(R.string.edit_habit_delete).performClick()
        composeTestRule.onNodeWithTextId(R.string.edit_confirm).performClick()
        navController.assertCurrentRouteName(HabitsNavigationDestination.HOME.route)
    }

    // verify back navigation is shown on correct pages

    @Test
    fun navHost_verifyBackNavigationNotShownOnHome() {
        composeTestRule.onNodeWithContentDescriptionId(R.string.topbar_back).assertDoesNotExist()
    }

    @Test
    fun navHost_verifyBackNavigationShownOnAddHabit() {
        navigateToAddHabitScreen()
        composeTestRule.onNodeWithContentDescriptionId(R.string.topbar_back).assertExists()
    }

    @Test
    fun navHost_verifyBackNavigationShownOnLogbook() {
        navigateToLogbookScreen()
        composeTestRule.onNodeWithContentDescriptionId(R.string.topbar_back).assertExists()
    }

    @Test
    fun navHost_verifyBackNavigationShownOnSettings() {
        navigateToSettingsScreen()
        composeTestRule.onNodeWithContentDescriptionId(R.string.topbar_back).assertExists()
    }

    @Test
    fun navHost_verifyBackNavigationShownOnAbout() {
        navigateToSettingsScreen()
        navigateToAboutScreen()
        composeTestRule.onNodeWithContentDescriptionId(R.string.topbar_back).assertExists()
    }

    @Test
    fun navHost_verifyBackNavigationShownOnDetail() {
        navigateToAddHabitScreen()
        addHabit()
        navigateToDetailScreen()
        composeTestRule.onNodeWithContentDescriptionId(R.string.topbar_back).assertExists()
    }

    @Test
    fun navHost_verifyBackNavigationShownOnEditHabit() {
        navigateToAddHabitScreen()
        addHabit()
        navigateToDetailScreen()
        navigateToEditHabitScreen()
        composeTestRule.onNodeWithContentDescriptionId(R.string.topbar_back).assertExists()
    }

    // verify back navigation behaviour

    @Test
    fun navHost_verifyBackNavigationNavigatesToHomeFromAddHabit() {
        navigateToAddHabitScreen()
        navigateBack()
        navController.assertCurrentRouteName(HabitsNavigationDestination.HOME.route)
    }

    @Test
    fun navHost_verifyBackNavigationNavigatesToHomeFromLogbook() {
        navigateToLogbookScreen()
        navigateBack()
        navController.assertCurrentRouteName(HabitsNavigationDestination.HOME.route)
    }

    @Test
    fun navHost_verifyBackNavigationNavigatesToHomeFromSettings() {
        navigateToSettingsScreen()
        navigateBack()
        navController.assertCurrentRouteName(HabitsNavigationDestination.HOME.route)
    }

    @Test
    fun navHost_verifyBackNavigationNavigatesToHomeFromDetail() {
        navigateToAddHabitScreen()
        addHabit()
        navigateToDetailScreen()
        navigateBack()
        navController.assertCurrentRouteName(HabitsNavigationDestination.HOME.route)
    }

    @Test
    fun navHost_verifyBackNavigationNavigatesToDetailFromEditHabit() {
        navigateToAddHabitScreen()
        addHabit()
        navigateToDetailScreen()
        navigateToEditHabitScreen()
        navigateBack()
        navController.assertCurrentRouteName(HabitsNavigationDestination.DETAIL.route, "habitId")
    }

    @Test
    fun navHost_verifyBackNavigationNavigatesToSettingsFromAbout() {
        navigateToSettingsScreen()
        navigateToAboutScreen()
        navigateBack()
        navController.assertCurrentRouteName(HabitsNavigationDestination.SETTINGS.route)
    }

    // helper functions

    private fun navigateToAddHabitScreen() {
        composeTestRule.onNodeWithContentDescriptionId(R.string.home_add_habit).performClick()
    }

    private fun navigateToLogbookScreen() {
        composeTestRule.onNodeWithContentDescriptionId(R.string.home_logbook).performClick()
    }

    private fun navigateToSettingsScreen() {
        composeTestRule.onNodeWithContentDescriptionId(R.string.settings).performClick()
    }

    private fun navigateToAboutScreen() {
        composeTestRule.onNodeWithContentDescriptionId(R.string.settings_about_screen)
            .performClick()
    }

    private fun navigateToDetailScreen() {
        composeTestRule.onNodeWithText(habit1.name).performClick()
        composeTestRule.onNodeWithContentDescriptionId(R.string.home_detail).performClick()
    }

    private fun navigateToEditHabitScreen() {
        composeTestRule.onNodeWithContentDescriptionId(R.string.detail_edit_habit).performClick()
    }

    private fun navigateBack() {
        composeTestRule.onNodeWithContentDescriptionId(R.string.topbar_back).performClick()
    }

    private fun addHabit() {
        composeTestRule.onNodeWithTextId(R.string.modify_habit_name).performClick()
            .performTextInput(habit1.name)
        composeTestRule.onNodeWithContentDescriptionId(R.string.add_habit_add_habit).performClick()
    }

}