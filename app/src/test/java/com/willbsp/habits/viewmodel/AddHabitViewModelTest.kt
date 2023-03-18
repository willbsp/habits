package com.willbsp.habits.viewmodel

import com.willbsp.habits.data.model.HabitFrequency
import com.willbsp.habits.fake.repository.FakeHabitRepository
import com.willbsp.habits.rules.TestDispatcherRule
import com.willbsp.habits.ui.common.ModifyHabitUiState
import com.willbsp.habits.ui.screens.add.AddHabitViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddHabitViewModelTest {

    // TODO need tests for validation

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    private val habitRepository = FakeHabitRepository()
    private lateinit var viewModel: AddHabitViewModel

    @Before
    fun setup() {
        viewModel = AddHabitViewModel(habitRepository)
    }

    @Test
    fun uiState_whenInitialised_defaultsSet() {
        assertEquals("", viewModel.uiState.name)
        assertEquals(HabitFrequency.DAILY, viewModel.uiState.frequency)
    }

    @Test
    fun uiState_whenUpdated_newStateSet() {
        val expected = ModifyHabitUiState("Reading", false, HabitFrequency.WEEKLY)
        val updatedUiState =
            viewModel.uiState.copy(name = "Reading", frequency = HabitFrequency.WEEKLY)
        viewModel.updateUiState(updatedUiState)
        assertEquals(expected, viewModel.uiState)
    }

    @Test
    fun uiState_whenFrequencyUpdated_frequencySet() {
        val updatedUiState = viewModel.uiState.copy(frequency = HabitFrequency.WEEKLY)
        viewModel.updateUiState(updatedUiState)
        assertEquals(HabitFrequency.WEEKLY, viewModel.uiState.frequency)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun saveHabit_whenStateSaved_saved() = runTest {
        val updatedUiState = viewModel.uiState.copy(name = "Reading")
        viewModel.updateUiState(updatedUiState)
        viewModel.saveHabit()
        assertTrue(habitRepository.habits.any { it.name == "Reading" })
    }

}