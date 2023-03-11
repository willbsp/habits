package com.willbsp.habits.fake

import com.willbsp.habits.TestData
import com.willbsp.habits.data.model.Entry
import com.willbsp.habits.data.repository.EntryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class FakeEntryRepository : EntryRepository {

    val entries = mutableListOf<Entry>()
    private var observableEntries = MutableStateFlow<List<Entry>>(listOf())
    private suspend fun emit() = observableEntries.emit(entries.toList())
    suspend fun populate() {
        entries.addAll(TestData.entryList)
        emit()
    }

    override fun getAllEntriesStream(): Flow<List<Entry>> {
        return observableEntries
    }

    override fun getAllEntriesStream(habitId: Int): Flow<List<Entry>> {
        return observableEntries.map { entry -> entry.filter { it.habitId == habitId } }
    }

    override suspend fun getEntry(date: LocalDate, habitId: Int): Entry? {
        return entries.find { it.date == date && it.habitId == habitId }
    }

    override suspend fun getOldestEntry(habitId: Int): Entry? {
        return entries.filter { it.habitId == habitId }.sortedBy { it.date }.findLast { true }
    }

    override suspend fun toggleEntry(habitId: Int, date: LocalDate) {
        val entry = entries.find { it.habitId == habitId && it.date == date }
        if (entry == null) {
            entries.add(Entry(entries.lastIndex + 1, habitId, date))
        } else {
            entries.remove(entry)
        }
        emit()
    }

}