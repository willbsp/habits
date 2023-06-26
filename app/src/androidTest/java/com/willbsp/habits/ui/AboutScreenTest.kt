package com.willbsp.habits.ui

import androidx.compose.material3.Surface
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.willbsp.habits.BuildConfig
import com.willbsp.habits.HiltComponentActivity
import com.willbsp.habits.ui.screens.about.AboutScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AboutScreenTest {

    @get:Rule(order = 1)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
        composeTestRule.setContent {
            Surface {
                AboutScreen(navigateUp = {})
            }
        }
    }

    @Test
    fun correctVersionShown() {
        composeTestRule.onNodeWithText(BuildConfig.VERSION_NAME, substring = true).assertExists()
    }

}