package com.willbsp.habits.repository

import com.willbsp.habits.data.TestData.habit1
import com.willbsp.habits.data.TestData.habit2
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.model.HabitFrequency
import com.willbsp.habits.data.repository.HabitRepository
import com.willbsp.habits.data.repository.local.LocalHabitRepository
import com.willbsp.habits.fake.dao.FakeHabitDao
import com.willbsp.habits.fake.repository.FakeHabitRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
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
        fun classes(): List<KClass<out HabitRepository>> {
            return listOf(
                LocalHabitRepository::class,
                FakeHabitRepository::class
            )
        }
    }

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

    @Test
    fun getAllHabitsStream_whenHabitsAdded_returnsHabits() = runTest {
        val habitsStream = repository.getAllHabitsStream()
        repository.upsertHabit(habit1)
        assertEquals(listOf(habit1), habitsStream.first())
        repository.upsertHabit(habit2)
        assertEquals(listOf(habit1, habit2), habitsStream.first())
    }

    @Test
    fun getAllHabitsStream_whenHabitsDeleted_returnsEmptyList() = runTest {
        val habitsStream = repository.getAllHabitsStream()
        repository.upsertHabit(habit1)
        repository.upsertHabit(habit2)
        assertEquals(listOf(habit1, habit2), habitsStream.first())
        repository.deleteHabit(habit1.id)
        repository.deleteHabit(habit2.id)
        assertEquals(listOf<Habit>(), habitsStream.first())
    }

    @Test
    fun getHabitStream_whenHabitUpdated_returnUpdatedHabit() = runTest {
        val habitStream = repository.getHabitStream(habit1.id)
        repository.upsertHabit(habit1)
        assertEquals(habit1, habitStream.first())
        val newHabit = habit1.copy(frequency = HabitFrequency.WEEKLY)
        repository.upsertHabit(newHabit)
        assertEquals(newHabit, habitStream.first())
    }

    @Test
    fun getHabitStream_whenHabitDeleted_returnNull() = runTest {
        val habitStream = repository.getHabitStream(habit1.id)
        repository.upsertHabit(habit1)
        assertEquals(habit1, habitStream.first())
        repository.deleteHabit(habit1.id)
        assertNull(habitStream.first())
    }

    @Test
    fun getHabit_whenExists_returnsHabit() = runTest {
        repository.upsertHabit(habit1)
        val habit = repository.getHabit(habit1.id)
        assertEquals(habit1, habit)
    }

    @Test
    fun getHabit_whenNoHabit_returnsNull() = runTest {
        assertNull(repository.getHabit(habit1.id))
        repository.upsertHabit(habit1)
        repository.deleteHabit(habit1.id)
        assertNull(repository.getHabit(habit1.id))
    }

    @Test
    fun addHabit_whenHabitAdded_habitExists() = runTest {
        repository.upsertHabit(habit1)
        assertEquals(habit1, repository.getHabit(habit1.id))
    }

    @Test
    fun updateHabit_whenHabitUpdated_habitUpdates() = runTest {
        repository.upsertHabit(habit1)
        val newHabit = habit1.copy(name = "Walking")
        repository.upsertHabit(newHabit)
        assertEquals(newHabit, repository.getHabit(newHabit.id))
    }

    @Test
    fun deleteHabit_whenHabitDeleted_isNull() = runTest {
        repository.upsertHabit(habit1)
        repository.deleteHabit(habit1.id)
        assertNull(repository.getHabit(habit1.id))
    }

}