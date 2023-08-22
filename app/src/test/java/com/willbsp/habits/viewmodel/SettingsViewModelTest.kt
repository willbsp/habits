package com.willbsp.habits.viewmodel

import com.willbsp.habits.domain.usecase.ExportDatabaseUseCase
import com.willbsp.habits.domain.usecase.ImportDatabaseUseCase
import com.willbsp.habits.fake.dao.FakeRawDao
import com.willbsp.habits.fake.repository.FakeSettingsRepository
import com.willbsp.habits.fake.util.FakeDatabaseUtils
import com.willbsp.habits.rules.TestDispatcherRule
import com.willbsp.habits.ui.screens.settings.SettingsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    @get:Rule
    val testRule = TestDispatcherRule()

    private val settingsRepository = FakeSettingsRepository()
    private val databaseUtils = FakeDatabaseUtils()
    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setup() {
        val dispatcher = testRule.getDispatcher()
        val importDatabaseUseCase = ImportDatabaseUseCase(databaseUtils, dispatcher)
        val exportDatabaseUseCase = ExportDatabaseUseCase(databaseUtils, FakeRawDao(), dispatcher)
        viewModel =
            SettingsViewModel(settingsRepository, exportDatabaseUseCase, importDatabaseUseCase)
    }

    @Test
    fun uiState_whenShowStatistic_thenTrue() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        settingsRepository.saveStatisticPreference(true)
        assertTrue(viewModel.uiState.value.showStatistic)
        collectJob.cancel()
    }

    @Test
    fun uiState_whenNotShowStatistic_thenFalse() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        settingsRepository.saveStatisticPreference(false)
        assertFalse(viewModel.uiState.value.showStatistic)
        collectJob.cancel()
    }

    @Test
    fun uiState_whenStatisticPreferenceDoesNotExist_thenTrue() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        assertTrue(viewModel.uiState.value.showStatistic)
        collectJob.cancel()
    }

    @Test
    fun uiState_whenShowSubtitle_thenTrue() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        settingsRepository.saveSubtitlePreference(true)
        assertTrue(viewModel.uiState.value.showCompletedSubtitle)
        collectJob.cancel()
    }

    @Test
    fun uiState_whenNotShowSubtitle_thenFalse() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        settingsRepository.saveSubtitlePreference(false)
        assertFalse(viewModel.uiState.value.showCompletedSubtitle)
        collectJob.cancel()
    }

    @Test
    fun uiState_whenSubtitlePreferenceDoesNotExist_thenTrue() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        assertTrue(viewModel.uiState.value.showCompletedSubtitle)
        collectJob.cancel()
    }

    @Test
    fun uiState_whenShowScore_thenTrue() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        settingsRepository.saveScorePreference(true)
        assertTrue(viewModel.uiState.value.showScore)
        collectJob.cancel()
    }

    @Test
    fun uiState_whenNotShowScore_thenFalse() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        settingsRepository.saveScorePreference(false)
        assertFalse(viewModel.uiState.value.showScore)
        collectJob.cancel()
    }

    @Test
    fun uiState_whenScorePreferenceDoesNotExist_thenFalse() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        assertFalse(viewModel.uiState.value.showScore)
        collectJob.cancel()
    }

    @Test
    fun saveStatisticPreference_whenSaved_thenUpdateUiState() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        assertTrue(viewModel.uiState.value.showStatistic)
        viewModel.saveStatisticPreference(false)
        assertFalse(viewModel.uiState.value.showStatistic)
        collectJob.cancel()
    }

    @Test
    fun saveSubtitlePreference_whenSaved_thenUpdateUiState() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        assertTrue(viewModel.uiState.value.showCompletedSubtitle)
        viewModel.saveSubtitlePreference(false)
        assertFalse(viewModel.uiState.value.showCompletedSubtitle)
        collectJob.cancel()
    }

    @Test
    fun saveScorePreference_whenSaved_thenUpdateUiState() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        assertFalse(viewModel.uiState.value.showScore)
        viewModel.saveScorePreference(true)
        assertTrue(viewModel.uiState.value.showScore)
        collectJob.cancel()
    }

    @Test
    fun exportDatabase_createsIdenticalFile() = runTest {
        val destination = File.createTempFile("test", ".db")
        viewModel.exportDatabase(destination.outputStream())
        assertEquals(FakeDatabaseUtils.TEST_FILE_TEXT, destination.readLines()[0])
    }

    @Test
    fun importDatabase_createsIdenticalFile() = runTest {
        val testText = "completely different text"
        val source = File.createTempFile("test", ".db")
        source.writeText(testText)
        viewModel.importDatabase(source.inputStream()) {}
        val destination = databaseUtils.getDatabasePath()
        assertEquals(testText, destination.readText())
    }

}