package com.willbsp.habits.fake.dao

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

    override fun getAllEntriesStream(): Flow<List<Entry>> = observableEntries

    override fun getAllEntriesStream(habitId: Int): Flow<List<Entry>> =
        observableEntries.map { entries -> entries.filter { it.habitId == habitId } }

    override suspend fun getEntryForDate(habitId: Int, date: LocalDate): Entry? =
        entries.find { it.date == date && it.habitId == habitId }

    override suspend fun getOldestEntry(habitId: Int): Entry? =
        entries.filter { it.habitId == habitId }.minByOrNull { it.date }

    override suspend fun insert(entry: Entry) {
        entries.add(entry)
        emit()
    }

    override suspend fun delete(entry: Entry) {
        entries.removeAll { it == entry }
        emit()
    }

}