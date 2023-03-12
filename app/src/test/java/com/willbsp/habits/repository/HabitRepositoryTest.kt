package com.willbsp.habits.repository

import com.willbsp.habits.TestData.habit1
import com.willbsp.habits.TestData.habit2
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.model.HabitFrequency
import com.willbsp.habits.data.repository.HabitRepository
import com.willbsp.habits.data.repository.local.LocalHabitRepository
import com.willbsp.habits.fake.FakeHabitDao
import com.willbsp.habits.fake.FakeHabitRepository
import com.willbsp.habits.rules.TestDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import kotlin.reflect.KClass

@RunWith(Parameterized::class)
class HabitRepositoryTest(
    private val repositoryClass: KClass<HabitRepository>
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): List<KClass<out HabitRepository>> {
            return listOf(
                LocalHabitRepository::class,
                FakeHabitRepository::class
            )
        }
    }

    @get:Rule
    val testDispatcher = TestDispatcherRule()
    private lateinit var repository: HabitRepository

    @Before
    fun setup() {
        when (repositoryClass) {
            LocalHabitRepository::class ->
                repository = LocalHabitRepository(FakeHabitDao())
            FakeHabitRepository::class ->
                repository = FakeHabitRepository()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getAllHabitsStream_whenHabitsAdded_returnsHabits() = runTest {
        val habitsStream = repository.getAllHabitsStream()
        repository.addHabit(habit1)
        assertEquals(listOf(habit1), habitsStream.first())
        repository.addHabit(habit2)
        assertEquals(listOf(habit1, habit2), habitsStream.first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getAllHabitsStream_whenHabitsDeleted_returnsEmptyList() = runTest {
        val habitsStream = repository.getAllHabitsStream()
        repository.addHabit(habit1)
        repository.addHabit(habit2)
        assertEquals(listOf(habit1, habit2), habitsStream.first())
        repository.deleteHabit(habit1.id)
        repository.deleteHabit(habit2.id)
        assertEquals(listOf<Habit>(), habitsStream.first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getHabitStream_whenHabitUpdated_returnUpdatedHabit() = runTest {
        val habitStream = repository.getHabitStream(habit1.id)
        repository.addHabit(habit1)
        assertEquals(habit1, habitStream.first())
        val newHabit = habit1.copy(frequency = HabitFrequency.WEEKLY)
        repository.updateHabit(newHabit)
        assertEquals(newHabit, habitStream.first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getHabitStream_whenHabitDeleted_returnNull() = runTest {
        val habitStream = repository.getHabitStream(habit1.id)
        repository.addHabit(habit1)
        assertEquals(habit1, habitStream.first())
        repository.deleteHabit(habit1.id)
        assertNull(habitStream.first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getHabit_whenExists_returnsHabit() = runTest {
        repository.addHabit(habit1)
        val habit = repository.getHabit(habit1.id)
        assertEquals(habit1, habit)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getHabit_whenNoHabit_returnsNull() = runTest {
        assertNull(repository.getHabit(habit1.id))
        repository.addHabit(habit1)
        repository.deleteHabit(habit1.id)
        assertNull(repository.getHabit(habit1.id))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun addHabit_whenHabitAdded_habitExists() = runTest {
        repository.addHabit(habit1)
        assertEquals(habit1, repository.getHabit(habit1.id))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun updateHabit_whenHabitUpdated_habitUpdates() = runTest {
        repository.addHabit(habit1)
        val newHabit = habit1.copy(name = "Walking")
        repository.updateHabit(newHabit)
        assertEquals(newHabit, repository.getHabit(newHabit.id))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteHabit_whenHabitDeleted_isNull() = runTest {
        repository.addHabit(habit1)
        repository.deleteHabit(habit1.id)
        assertNull(repository.getHabit(habit1.id))
    }

}