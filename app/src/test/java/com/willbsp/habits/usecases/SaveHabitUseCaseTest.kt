package com.willbsp.habits.usecases

import com.willbsp.habits.data.TestData.habit1
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.model.HabitFrequency
import com.willbsp.habits.domain.usecase.SaveHabitUseCase
import com.willbsp.habits.fake.repository.FakeHabitRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SaveHabitUseCaseTest {

    private lateinit var habitRepository: FakeHabitRepository
    private lateinit var saveHabitUseCase: SaveHabitUseCase

    @Before
    fun setup() {
        habitRepository = FakeHabitRepository()
        saveHabitUseCase = SaveHabitUseCase(habitRepository)
    }

    @Test
    fun saveHabit_validHabit_addNewHabit() = runTest {
        assertTrue(saveHabitUseCase(habit1, this))
        advanceUntilIdle()
        assertEquals(habit1, habitRepository.getHabit(habit1.id))
    }

    @Test
    fun saveHabit_validHabit_updatesHabit() = runTest {
        habitRepository.upsertHabit(habit1)
        val updatedHabit = habit1.copy(name = "Walking")
        assertTrue(saveHabitUseCase(updatedHabit, this))
        advanceUntilIdle()
        assertEquals(updatedHabit, habitRepository.getAllHabitsStream().first().first())
    }

    @Test
    fun saveHabit_invalidHabit_noHabitAdded() = runTest {
        val invalid1 =
            Habit(id = 0, name = "this is a really long name", frequency = HabitFrequency.DAILY)
        val invalid2 =
            Habit(id = 1, name = "w", frequency = HabitFrequency.DAILY)
        assertFalse(saveHabitUseCase(invalid1, this))
        assertFalse(saveHabitUseCase(invalid2, this))
        advanceUntilIdle()
        assertNull(habitRepository.getHabit(invalid1.id))
        assertNull(habitRepository.getHabit(invalid2.id))
    }

}