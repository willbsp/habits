package com.willbsp.habits.repository

import com.willbsp.habits.data.TestData.habit1
import com.willbsp.habits.data.TestData.habit2
import com.willbsp.habits.data.model.Entry
import com.willbsp.habits.data.model.HabitWithEntries
import com.willbsp.habits.data.repository.HabitWithEntriesRepository
import com.willbsp.habits.data.repository.local.LocalHabitWithEntriesRepository
import com.willbsp.habits.fake.*
import com.willbsp.habits.fake.dao.FakeEntryDao
import com.willbsp.habits.fake.dao.FakeHabitDao
import com.willbsp.habits.fake.dao.FakeHabitWithEntriesDao
import com.willbsp.habits.fake.repository.FakeHabitWithEntriesRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import java.time.LocalDate
import kotlin.reflect.KClass

@RunWith(Parameterized::class)
class HabitWithEntriesRepositoryTest(
    private val repositoryClass: KClass<HabitWithEntriesRepository>
) {

    companion object {
        @JvmStatic
        @Parameters
        fun classes(): List<KClass<out HabitWithEntriesRepository>> {
            return listOf(
                LocalHabitWithEntriesRepository::class,
                FakeHabitWithEntriesRepository::class
            )
        }
    }

    private lateinit var repository: HabitWithEntriesRepository
    private val date = LocalDate.parse("2023-03-10")
    private val habitDao = FakeHabitDao()
    private val entryDao = FakeEntryDao()

    @Before
    fun setup() {
        val habitWithEntriesDao = FakeHabitWithEntriesDao(habitDao, entryDao)
        when (repositoryClass) {
            LocalHabitWithEntriesRepository::class ->
                repository = LocalHabitWithEntriesRepository(habitWithEntriesDao)

            FakeHabitWithEntriesRepository::class ->
                repository = FakeHabitWithEntriesRepository(habitWithEntriesDao)
        }
    }

    @Test
    fun getHabitsWithEntries_whenNoHabits_returnEmptyList() = runTest {
        assertEquals(listOf<HabitWithEntries>(), repository.getHabitsWithEntries().first())
    }

    @Test
    fun getHabitsWithEntries_whenHabitsButNoEntries_returnHabitsAndEmptyList() = runTest {
        habitDao.upsert(habit1)
        habitDao.upsert(habit2)
        assertEquals(
            listOf(HabitWithEntries(habit1, listOf()), HabitWithEntries(habit2, listOf())),
            repository.getHabitsWithEntries().first()
        )
    }

    @Test
    fun getHabitsWithEntries_whenHabitsAndEntries_returnHabitsAndEntries() = runTest {
        val entry = Entry(id = 0, habitId = habit1.id, date)
        habitDao.upsert(habit1)
        entryDao.insert(Entry(habitId = habit1.id, date = date))
        assertEquals(
            listOf(HabitWithEntries(habit1, listOf(entry))),
            repository.getHabitsWithEntries().first()
        )
    }

}