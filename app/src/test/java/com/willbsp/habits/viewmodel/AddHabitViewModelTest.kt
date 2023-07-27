package com.willbsp.habits.viewmodel

import com.willbsp.habits.data.model.HabitFrequency
import com.willbsp.habits.domain.usecase.ValidateHabitNameUseCase
import com.willbsp.habits.fake.repository.FakeHabitRepository
import com.willbsp.habits.rules.TestDispatcherRule
import com.willbsp.habits.ui.common.HabitUiState
import com.willbsp.habits.ui.screens.add.AddHabitViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddHabitViewModelTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    private val habitRepository = FakeHabitRepository()
    private lateinit var viewModel: AddHabitViewModel

    @Before
    fun setup() {
        viewModel = AddHabitViewModel(habitRepository, ValidateHabitNameUseCase())
    }

    @Test
    fun uiState_whenInitialised_defaultsSet() {
        assertEquals("", viewModel.uiState.name)
        assertEquals(HabitFrequency.DAILY, viewModel.uiState.frequency)
    }

    @Test
    fun uiState_whenUpdated_newStateSet() {
        val expected = HabitUiState.Habit("Reading", false, HabitFrequency.WEEKLY)
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

    @Test
    fun uiState_whenStateInvalid_stateInvalid() {
        val uiState = viewModel.uiState.copy(name = "this is a really long name hi hi")
        viewModel.updateUiState(uiState)
        assertTrue(viewModel.uiState.nameIsInvalid)
    }

    @Test
    fun saveHabit_whenStateValid_saved() {
        val updatedUiState = viewModel.uiState.copy(name = "Reading")
        viewModel.updateUiState(updatedUiState)
        assertTrue(viewModel.saveHabit())
        assertTrue(habitRepository.habits.any { it.name == "Reading" })
    }

    @Test
    fun saveHabit_whenStateInvalid_notSaved() {
        val uiState = viewModel.uiState.copy(name = "this is a really long name hi hi")
        viewModel.updateUiState(uiState)
        assertFalse(viewModel.saveHabit())
        assertFalse(habitRepository.habits.any { it.name == uiState.name })
    }

}