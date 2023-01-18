package com.willbsp.habits.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.willbsp.habits.R
import com.willbsp.habits.helper.assertCurrentRouteName
import com.willbsp.habits.ui.navigation.HabitsNavigationDestination
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HabitsNavigationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var navController: TestNavHostController

    private fun navigateToAddHabitScreen() {
        val addText = composeTestRule.activity.getString(R.string.home_screen_add_habit)
        composeTestRule.onNodeWithContentDescription(addText).performClick()
    }

    // TODO implement test tags

    @Before
    fun setupHabitsNavHost() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
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

    /*@Test
    fun navHost_addHabit_displayedHabitOnHome() {
        navigateToAddHabitScreen()
        val nameText = composeTestRule.activity.getString(R.string.add_habit_name)
        composeTestRule.onNodeWithText(nameText).performClick().performTextInput("Swimming")
        val doneText = composeTestRule.activity.getString(R.string.add_habit_add_habit)
        composeTestRule.onNodeWithContentDescription(doneText).performClick()
        composeTestRule.onNodeWithText("Swimming").assertExists()
    }*/
    // should be for testing state, not navigation TODO


}