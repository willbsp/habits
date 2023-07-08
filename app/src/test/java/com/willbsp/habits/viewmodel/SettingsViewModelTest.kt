package com.willbsp.habits.viewmodel

import com.willbsp.habits.fake.repository.FakeSettingsRepository
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

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    private val settingsRepository = FakeSettingsRepository()
    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setup() {
        viewModel = SettingsViewModel(settingsRepository)
    }

    @Test
    fun uiState_whenShowStreaks_thenTrue() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        settingsRepository.saveStatisticPreference(true)
        assertTrue(viewModel.uiState.value.showStatistic)
        collectJob.cancel()
    }

    @Test
    fun uiState_whenNotShowStreaks_thenFalse() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        settingsRepository.saveStatisticPreference(false)
        assertFalse(viewModel.uiState.value.showStatistic)
        collectJob.cancel()
    }

    @Test
    fun uiState_whenStreaksPreferenceDoesNotExist_thenTrue() = runTest {
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
    fun saveStreaksPreference_whenSaved_thenUpdateUiState() = runTest {
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

}