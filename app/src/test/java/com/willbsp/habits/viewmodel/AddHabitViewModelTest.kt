package com.willbsp.habits.viewmodel

import com.willbsp.habits.data.model.HabitFrequency
import com.willbsp.habits.fake.FakeDataSource
import com.willbsp.habits.fake.FakeHabitRepository
import com.willbsp.habits.ui.add.AddHabitViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AddHabitViewModelTest {

    private lateinit var fakeRepository: FakeHabitRepository
    private lateinit var addHabitViewModel: AddHabitViewModel

    @Before
    fun createViewModel() {
        fakeRepository = FakeHabitRepository()
        addHabitViewModel = AddHabitViewModel(
            habitsRepository = fakeRepository
        )
    }

    @Test
    fun viewModelInitialValue_defaultsSet() {
        assertEquals("", addHabitViewModel.habitUiState.name)
        assertEquals(HabitFrequency.DAILY, addHabitViewModel.habitUiState.frequency)
    }

    @Test
    fun viewModelUpdateUiState_updatedName() {
        val updatedUiState = addHabitViewModel.habitUiState.copy(name = "Reading")
        addHabitViewModel.updateUiState(updatedUiState)
        assertEquals("Reading", addHabitViewModel.habitUiState.name)
    }

    @Test
    fun viewModelUpdateUiState_updatedFrequency() {
        val updatedUiState = addHabitViewModel.habitUiState.copy(frequency = HabitFrequency.WEEKLY)
        addHabitViewModel.updateUiState(updatedUiState)
        assertEquals(HabitFrequency.WEEKLY, addHabitViewModel.habitUiState.frequency)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun viewModelSaveHabit_savedHabit() = runTest {
        val updatedUiState = addHabitViewModel.habitUiState.copy(name = "Reading")
        addHabitViewModel.updateUiState(updatedUiState)
        addHabitViewModel.saveHabit()
        assertEquals(true, FakeDataSource.habitExists("Reading"))
    }

}