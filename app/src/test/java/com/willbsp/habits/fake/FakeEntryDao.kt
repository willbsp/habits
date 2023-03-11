package com.willbsp.habits.fake

import com.willbsp.habits.TestData
import com.willbsp.habits.data.database.dao.EntryDao
import com.willbsp.habits.data.model.Entry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class FakeEntryDao : EntryDao {

    val entries = mutableListOf<Entry>()
    private var observableEntries = MutableStateFlow<List<Entry>>(listOf())
    private suspend fun emit() = observableEntries.emit(entries.toList())
    suspend fun populate() {
        entries.addAll(TestData.entryList)
        emit()
    }

    override fun getAllEntries(): Flow<List<Entry>> = observableEntries

    override fun getEntriesForHabit(habitId: Int): Flow<List<Entry>> =
        observableEntries.map { entry -> entry.filter { it.habitId == habitId } }

    override suspend fun getEntryForDate(habitId: Int, date: LocalDate): Entry? =
        entries.find { it.date == date && it.habitId == habitId }

    override suspend fun getOldestEntry(habitId: Int): Entry? =
        entries.filter { it.habitId == habitId }.minByOrNull { it.date }

    override suspend fun insert(entry: Entry) {
        entries.add(entry)
        emit()
    }

    override suspend fun update(entry: Entry) {
        val index = entries.indexOfFirst { it.id == entry.id }
        if (index != -1) entries[index] = entry
        emit()
    }

    override suspend fun delete(entry: Entry) {
        entries.removeAll { it == entry }
        emit()
    }

}