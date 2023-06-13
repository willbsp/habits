package com.willbsp.habits.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.willbsp.habits.HiltComponentActivity
import com.willbsp.habits.helper.assertCurrentRouteName
import com.willbsp.habits.ui.navigation.HabitsNavigationDestination
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.willbsp.habits.R
import com.willbsp.habits.data.TestData.habit1
import com.willbsp.habits.helper.onNodeWithContentDescriptionId
import com.willbsp.habits.helper.onNodeWithTextId

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class HabitsNavigationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()
    private lateinit var navController: NavHostController

    @Before
    fun init() {
        hiltRule.inject()
        setupHabitsNavHost()
    }

    @OptIn(ExperimentalAnimationApi::class)
    private fun setupHabitsNavHost() {
        composeTestRule.setContent {
            navController = rememberAnimatedNavController()
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            HabitsApp(navController = navController)
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

    // verify back navigation behaviour

    @Test
    fun navHost_verifyAddHabitNavigatesBackToHome() {
        navigateToAddHabitScreen()
        addHabit()
        navController.assertCurrentRouteName(HabitsNavigationDestination.HOME.route)
    }

    @Test
    fun navHost_verifyBackNavigationNavigatesBackFromAddScreen() {
        navigateToAddHabitScreen()
        composeTestRule.onNodeWithContentDescriptionId(R.string.topbar_back).performClick()
        navController.assertCurrentRouteName(HabitsNavigationDestination.HOME.route)
    }

    @Test
    fun navHost_verifyBackNavigationNotShownOnHome() {
        composeTestRule.onNodeWithContentDescriptionId(R.string.topbar_back).assertDoesNotExist()
    }

    @Test
    fun navHost_verifyBackNavigationShownOnAddScreen() {
        navigateToAddHabitScreen()
        composeTestRule.onNodeWithContentDescriptionId(R.string.topbar_back).assertExists()
    }

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
        composeTestRule.onNodeWithContentDescriptionId(R.string.settings_about_screen).performClick()
    }

    private fun navigateToDetailScreen() {
        composeTestRule.onNodeWithText(habit1.name).performClick()
        composeTestRule.onNodeWithContentDescriptionId(R.string.home_detail).performClick()
    }

    private fun navigateToEditHabitScreen() {
        composeTestRule.onNodeWithContentDescriptionId(R.string.detail_edit_habit).performClick()
    }

    private fun addHabit() {
        composeTestRule.onNodeWithTextId(R.string.modify_habit_name).performClick().performTextInput(habit1.name)
        composeTestRule.onNodeWithContentDescriptionId(R.string.add_habit_add_habit).performClick()
    }

}