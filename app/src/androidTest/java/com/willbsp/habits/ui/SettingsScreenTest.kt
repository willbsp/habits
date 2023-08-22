package com.willbsp.habits.ui

import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.willbsp.habits.HiltComponentActivity
import com.willbsp.habits.R
import com.willbsp.habits.data.repository.SettingsRepository
import com.willbsp.habits.domain.usecase.ExportDatabaseUseCase
import com.willbsp.habits.domain.usecase.ImportDatabaseUseCase
import com.willbsp.habits.fake.dao.FakeRawDao
import com.willbsp.habits.fake.util.FakeDatabaseUtils
import com.willbsp.habits.helper.onNodeWithTextId
import com.willbsp.habits.ui.screens.settings.SettingsScreen
import com.willbsp.habits.ui.screens.settings.SettingsViewModel
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
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class SettingsScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()

    @Inject
    lateinit var settingsRepository: SettingsRepository

    @Before
    fun setup() {
        hiltRule.inject()
        val fakeDatabaseUtils = FakeDatabaseUtils()
        val importDatabaseUseCase = ImportDatabaseUseCase(fakeDatabaseUtils)
        val exportDatabaseUseCase = ExportDatabaseUseCase(fakeDatabaseUtils, FakeRawDao())
        composeTestRule.setContent {
            val viewModel =
                SettingsViewModel(settingsRepository, exportDatabaseUseCase, importDatabaseUseCase)
            Surface {
                val state by viewModel.uiState.collectAsStateWithLifecycle()
                SettingsScreen(
                    navigateUp = { },
                    navigateToAboutScreen = { },
                    onShowStatisticPressed = viewModel::saveStatisticPreference,
                    onShowSubtitlePressed = viewModel::saveSubtitlePreference,
                    onShowScorePressed = viewModel::saveScorePreference,
                    onExportPressed = {},
                    onImportPressed = {},
                    settingsUiState = state
                )
            }
        }
    }

    @Test
    fun displayStatistic_togglesSettings() = runTest {
        assertEquals(true, settingsRepository.getStatisticPreference().first())
        composeTestRule.onNodeWithTextId(R.string.settings_display_stats).performClick()
        assertEquals(false, settingsRepository.getStatisticPreference().first())
        composeTestRule.onNodeWithTextId(R.string.settings_display_stats).performClick()
        assertEquals(true, settingsRepository.getStatisticPreference().first())
    }

    @Test
    fun displaySubtitle_togglesSettings() = runTest {
        assertEquals(true, settingsRepository.getSubtitlePreference().first())
        composeTestRule.onNodeWithTextId(R.string.settings_completed_subtitle).performClick()
        assertEquals(false, settingsRepository.getSubtitlePreference().first())
        composeTestRule.onNodeWithTextId(R.string.settings_completed_subtitle).performClick()
        assertEquals(true, settingsRepository.getSubtitlePreference().first())
    }

    @Test
    fun displayScore_togglesSettings() = runTest {
        assertEquals(false, settingsRepository.getScorePreference().first())
        composeTestRule.onNodeWithTextId(R.string.settings_scores_on_home).performClick()
        assertEquals(true, settingsRepository.getScorePreference().first())
        composeTestRule.onNodeWithTextId(R.string.settings_scores_on_home).performClick()
        assertEquals(false, settingsRepository.getScorePreference().first())
    }

}