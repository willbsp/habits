package com.willbsp.habits.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.willbsp.habits.TestData.habit1
import com.willbsp.habits.data.model.HabitFrequency
import com.willbsp.habits.domain.SaveHabitUseCase
import com.willbsp.habits.fake.repository.FakeHabitRepository
import com.willbsp.habits.rules.TestDispatcherRule
import com.willbsp.habits.ui.common.HabitUiState
import com.willbsp.habits.ui.screens.edit.EditHabitViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

class EditHabitViewModelTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    private val habitRepository = FakeHabitRepository()
    private lateinit var viewModel: EditHabitViewModel

    @Before
    fun setup() {
        val saveHabitUseCase = SaveHabitUseCase(habitRepository)
        runBlocking { habitRepository.upsertHabit(habit1) }
        val savedStateHandle = SavedStateHandle(mapOf("habitId" to habit1.id))
        viewModel = EditHabitViewModel(habitRepository, saveHabitUseCase, savedStateHandle)
    }

    @Test
    fun uiState_whenLoaded_loadsHabit() {
        val uiState = viewModel.uiState as HabitUiState.Habit
        assertEquals(habit1.name, uiState.name)
        assertEquals(habit1.frequency, uiState.frequency)
    }

    @Test
    fun uiState_whenUpdated_newStateSet() {
        val expected = HabitUiState.Habit("Reading", false, HabitFrequency.WEEKLY)
        val updatedUiState =
            (viewModel.uiState as HabitUiState.Habit).copy(
                name = "Reading",
                frequency = HabitFrequency.WEEKLY
            )
        viewModel.updateUiState(updatedUiState)
        assertEquals(expected, viewModel.uiState)
    }

    @Test
    fun uiState_whenFrequencyUpdated_frequencySet() {
        val updatedUiState =
            (viewModel.uiState as HabitUiState.Habit).copy(frequency = HabitFrequency.WEEKLY)
        viewModel.updateUiState(updatedUiState)
        assertEquals(HabitFrequency.WEEKLY, (viewModel.uiState as HabitUiState.Habit).frequency)
    }

    @Test
    fun uiState_whenStateInvalid_stateInvalid() {
        val uiState =
            (viewModel.uiState as HabitUiState.Habit).copy(name = "this is a really long name hi hi")
        viewModel.updateUiState(uiState)
        assertTrue((viewModel.uiState as HabitUiState.Habit).nameIsInvalid)
    }

    @Test
    fun saveHabit_whenStateValid_saved() {
        val updatedUiState = (viewModel.uiState as HabitUiState.Habit).copy(name = "Reading")
        viewModel.updateUiState(updatedUiState)
        assertTrue(viewModel.saveHabit())
        assertTrue(habitRepository.habits.any { it.name == "Reading" })
    }

    @Test
    fun saveHabit_whenStateInvalid_notSaved() {
        val uiState =
            (viewModel.uiState as HabitUiState.Habit).copy(name = "this is a really long name hi hi")
        viewModel.updateUiState(uiState)
        assertFalse(viewModel.saveHabit())
        assertFalse(habitRepository.habits.any { it.name == uiState.name })
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteHabit_whenHabitDeleted_deleteHabit() = runTest {
        assertNotNull(habitRepository.getHabit(habit1.id))
        viewModel.deleteHabit()
        assertNull(habitRepository.getHabit(habit1.id))
    }

}