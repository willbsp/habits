package com.willbsp.habits.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavHostController
import androidx.navigation.NavigatorProvider
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.navigation.testing.TestNavigatorState
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.willbsp.habits.R
import com.willbsp.habits.HiltComponentActivity
import com.willbsp.habits.helper.assertCurrentRouteName
import com.willbsp.habits.ui.navigation.HabitsNavigationDestination
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

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

    @Test
    fun navHost_verifyStartDestination() {
        navController.assertCurrentRouteName(HabitsNavigationDestination.HOME.route)
    }

    @Test
    fun navHost_clickAddHabit_navigatesToAddHabitScreen() {
        navigateToAddHabitScreen()
        navController.assertCurrentRouteName(HabitsNavigationDestination.ADD.route)
    }

    @Test
    fun navHost_verifyBackNavigationNotShownOnHome() {
        val backText = composeTestRule.activity.getString(R.string.topbar_back)
        composeTestRule.onNodeWithContentDescription(backText).assertDoesNotExist()
    }

    @Test
    fun navHost_verifyBackNavigationShownOnAddScreen() {
        navigateToAddHabitScreen()
        val backText = composeTestRule.activity.getString(R.string.topbar_back)
        composeTestRule.onNodeWithContentDescription(backText).assertExists()
    }

    @Test
    fun navHost_verifyBackNavigationNavigatesBackFromAddScreen() {
        navigateToAddHabitScreen()
        val backText = composeTestRule.activity.getString(R.string.topbar_back)
        composeTestRule.onNodeWithContentDescription(backText).performClick()
        navController.assertCurrentRouteName(HabitsNavigationDestination.HOME.route)
    }

    @Test
    fun navHost_verifyAddHabitNavigatesBackToHome() {
        navigateToAddHabitScreen()
        val nameText = composeTestRule.activity.getString(R.string.modify_habit_name)
        composeTestRule.onNodeWithText(nameText).performClick().performTextInput("Swimming")
        val doneText = composeTestRule.activity.getString(R.string.add_habit_add_habit)
        composeTestRule.onNodeWithContentDescription(doneText).performClick()
        navController.assertCurrentRouteName(HabitsNavigationDestination.HOME.route)
    }

    private fun navigateToAddHabitScreen() {
        val addText = composeTestRule.activity.getString(R.string.home_add_habit)
        composeTestRule.onNodeWithContentDescription(addText).performClick()
    }

}