package com.willbsp.habits.repository

import com.willbsp.habits.data.TestData.habit1
import com.willbsp.habits.data.TestData.habit2
import com.willbsp.habits.data.model.Entry
import com.willbsp.habits.data.repository.EntryRepository
import com.willbsp.habits.data.repository.local.LocalEntryRepository
import com.willbsp.habits.fake.dao.FakeEntryDao
import com.willbsp.habits.fake.repository.FakeEntryRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.time.LocalDate
import kotlin.reflect.KClass

@RunWith(Parameterized::class)
class EntryRepositoryTest(
    private val repositoryClass: KClass<EntryRepository>
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun classes(): List<KClass<out EntryRepository>> {
            return listOf(
                LocalEntryRepository::class,
                FakeEntryRepository::class
            )
        }
    }

    private val date = LocalDate.parse("2023-03-10")
    private lateinit var repository: EntryRepository

    @Before
    fun setup() {
        when (repositoryClass) {
            LocalEntryRepository::class ->
                repository = LocalEntryRepository(FakeEntryDao())

            FakeEntryRepository::class ->
                repository = FakeEntryRepository()
        }
    }

    @Test
    fun getAllEntriesStream_whenEntriesAdded_returnsEntries() = runTest {
        val entryStream = repository.getAllEntriesStream()
        repository.toggleEntry(habit1.id, date)
        assertNotNull(entryStream.first().find { it.habitId == habit1.id && it.date == date })
        repository.toggleEntry(habit2.id, date)
        assertNotNull(entryStream.first().find { it.habitId == habit2.id && it.date == date })
    }

    @Test
    fun getAllEntriesStream_whenNoEntries_returnEmptyList() = runTest {
        val entryStream = repository.getAllEntriesStream()
        assertEquals(listOf<Entry>(), entryStream.first())
    }

    @Test
    fun getAllEntriesStream_whenEntriesForHabitAdded_returnEntries() = runTest {
        val entryStream = repository.getAllEntriesStream(habit1.id)
        repository.toggleEntry(habit1.id, date)
        repository.toggleEntry(habit2.id, date)
        assertTrue(entryStream.first().all { it.date == date && it.habitId == habit1.id })
    }

    @Test
    fun getAllEntriesStream_whenEntriesForHabitRemoved_returnEmpty() = runTest {
        val entryStream = repository.getAllEntriesStream(habit1.id)
        repository.toggleEntry(habit1.id, date)
        repository.toggleEntry(habit2.id, date)
        repository.toggleEntry(habit1.id, date)
        assertEquals(listOf<Entry>(), entryStream.first())
    }

    @Test
    fun getEntry_whenEntryExists_returnEntry() = runTest {
        repository.toggleEntry(habit1.id, date)
        assertNotNull(repository.getEntry(date, habit1.id))
    }

    @Test
    fun getEntry_whenEntryDoesNotExist_returnNull() = runTest {
        assertNull(repository.getEntry(date, habit1.id))
    }

    @Test
    fun getOldestEntry_whenEntriesExist_returnOldest() = runTest {
        repository.toggleEntry(habit1.id, date)
        repository.toggleEntry(habit1.id, date.minusDays(2f.toLong()))
        repository.toggleEntry(habit1.id, date.minusDays(5f.toLong()))
        repository.toggleEntry(habit1.id, date.minusDays(10f.toLong()))
        assertEquals(date.minusDays(10f.toLong()), repository.getOldestEntry(habit1.id)?.date)
    }

    @Test
    fun getOldestEntry_whenNoEntriesExist_returnNull() = runTest {
        assertNull(repository.getOldestEntry(habit1.id))
    }

    @Test
    fun toggleEntry_whenNoEntryExists_addEntry() = runTest {
        assertNull(repository.getEntry(date, habit1.id))
        repository.toggleEntry(habit1.id, date)
        assertEquals(date, repository.getEntry(date, habit1.id)?.date)
    }

    @Test
    fun toggleEntry_whenEntryExists_removeEntry() = runTest {
        repository.toggleEntry(habit1.id, date)
        assertEquals(date, repository.getEntry(date, habit1.id)?.date)
        repository.toggleEntry(habit1.id, date)
        assertNull(repository.getEntry(date, habit1.id))
    }

    @Test
    fun setEntry_completeEntryWhereEntryDoesNotExist_createsEntry() = runTest {
        assertNull(repository.getEntry(date, habit1.id))
        repository.setEntry(habit1.id, date, true)
        assertEquals(date, repository.getEntry(date, habit1.id)?.date)
    }

    @Test
    fun setEntry_completeEntryWhereEntryExists_noChange() = runTest {
        repository.toggleEntry(habit1.id, date)
        val entry = repository.getEntry(date, habit1.id)
        repository.setEntry(habit1.id, date, true)
        assertEquals(entry, repository.getEntry(date, habit1.id))
    }

    @Test
    fun setEntry_removeEntryWhereEntryExists_removesEntry() = runTest {
        repository.toggleEntry(habit1.id, date)
        assertNotNull(repository.getEntry(date, habit1.id))
        repository.setEntry(habit1.id, date, false)
        assertNull(repository.getEntry(date, habit1.id))
    }

    @Test
    fun setEntry_removeEntryWhereEntryDoesNotExist_noChange() = runTest {
        assertNull(repository.getEntry(date, habit1.id))
        repository.setEntry(habit1.id, date, false)
        assertNull(repository.getEntry(date, habit1.id))
    }

}