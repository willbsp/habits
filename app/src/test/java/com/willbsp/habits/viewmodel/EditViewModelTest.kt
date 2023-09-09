package com.willbsp.habits.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.willbsp.habits.data.TestData.habit1
import com.willbsp.habits.data.model.HabitFrequency
import com.willbsp.habits.domain.usecase.SaveHabitUseCase
import com.willbsp.habits.fake.repository.FakeHabitRepository
import com.willbsp.habits.fake.repository.FakeReminderRepository
import com.willbsp.habits.fake.util.FakeReminderManager
import com.willbsp.habits.rules.TestDispatcherRule
import com.willbsp.habits.ui.common.form.HabitFormUiState
import com.willbsp.habits.ui.screens.edit.EditViewModel
import com.willbsp.habits.util.ReminderManager
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EditViewModelTest {

    @get:Rule
    val testRule = TestDispatcherRule()

    private val habitRepository = FakeHabitRepository()
    private val reminderRepository = FakeReminderRepository()
    private val reminderManager: ReminderManager = FakeReminderManager()
    private lateinit var viewModel: EditViewModel

    @Before
    fun setup() {
        runBlocking { habitRepository.upsertHabit(habit1) }
        val savedStateHandle = SavedStateHandle(mapOf("habitId" to habit1.id))
        val saveHabitUseCase =
            SaveHabitUseCase(
                habitRepository,
                reminderRepository,
                reminderManager,
                testRule.getDispatcher()
            )
        viewModel =
            EditViewModel(
                habitRepository,
                reminderManager,
                reminderRepository,
                saveHabitUseCase,
                savedStateHandle
            )
    }

    @Test
    fun uiState_whenLoaded_loadsHabit() {
        val uiState = viewModel.uiState as HabitFormUiState.Data
        assertEquals(habit1.name, uiState.name)
        assertEquals(habit1.frequency, uiState.frequency)
    }

    @Test
    fun uiState_whenUpdated_newStateSet() {
        val expected = HabitFormUiState.Data("Reading", HabitFrequency.WEEKLY)
        val updatedUiState =
            (viewModel.uiState as HabitFormUiState.Data).copy(
                name = "Reading",
                frequency = HabitFrequency.WEEKLY
            )
        viewModel.updateUiState(updatedUiState)
        assertEquals(expected, viewModel.uiState)
    }

    @Test
    fun uiState_whenFrequencyUpdated_frequencySet() {
        val updatedUiState =
            (viewModel.uiState as HabitFormUiState.Data).copy(frequency = HabitFrequency.WEEKLY)
        viewModel.updateUiState(updatedUiState)
        assertEquals(
            HabitFrequency.WEEKLY,
            (viewModel.uiState as HabitFormUiState.Data).frequency
        )
    }

    @Test
    fun uiState_whenStateInvalid_stateInvalid() {
        val uiState =
            (viewModel.uiState as HabitFormUiState.Data).copy(name = "this is a really long name hi hi")
        viewModel.updateUiState(uiState)
        viewModel.saveHabit()
        assertTrue((viewModel.uiState as HabitFormUiState.Data).nameIsInvalid)
    }

    @Test
    fun saveHabit_whenStateValid_saved() {
        val updatedUiState =
            (viewModel.uiState as HabitFormUiState.Data).copy(name = "Reading")
        viewModel.updateUiState(updatedUiState)
        assertTrue(viewModel.saveHabit())
        assertTrue(habitRepository.habits.any { it.name == "Reading" })
    }

    @Test
    fun saveHabit_whenStateInvalid_notSaved() {
        val uiState =
            (viewModel.uiState as HabitFormUiState.Data).copy(name = "this is a really long name hi hi")
        viewModel.updateUiState(uiState)
        assertFalse(viewModel.saveHabit())
        assertFalse(habitRepository.habits.any { it.name == uiState.name })
    }

    @Test
    fun deleteHabit_whenHabitDeleted_deleteHabit() = runTest {
        assertNotNull(habitRepository.getHabit(habit1.id))
        viewModel.deleteHabit()
        assertNull(habitRepository.getHabit(habit1.id))
    }

}