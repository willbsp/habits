package com.willbsp.habits.viewmodel

import com.willbsp.habits.data.model.HabitFrequency
import com.willbsp.habits.domain.usecase.SaveHabitUseCase
import com.willbsp.habits.fake.repository.FakeHabitRepository
import com.willbsp.habits.fake.repository.FakeReminderRepository
import com.willbsp.habits.fake.util.FakeReminderManager
import com.willbsp.habits.rules.TestDispatcherRule
import com.willbsp.habits.ui.common.form.HabitFormUiState
import com.willbsp.habits.ui.screens.add.AddViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddViewModelTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    private val habitRepository = FakeHabitRepository()
    private val reminderRepository = FakeReminderRepository()
    private lateinit var viewModel: AddViewModel

    @Before
    fun setup() {
        val saveHabitUseCase = SaveHabitUseCase(
            habitRepository,
            reminderRepository,
            FakeReminderManager(),
            testDispatcher.getDispatcher()
        )
        viewModel = AddViewModel(saveHabitUseCase)
    }

    @Test
    fun uiState_whenInitialised_defaultsSet() {
        assertEquals("", viewModel.uiState.name)
        assertEquals(HabitFrequency.DAILY, viewModel.uiState.frequency)
    }

    @Test
    fun uiState_whenUpdated_newStateSet() {
        val expected = HabitFormUiState.Data("Reading", HabitFrequency.WEEKLY)
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
        viewModel.saveHabit()
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