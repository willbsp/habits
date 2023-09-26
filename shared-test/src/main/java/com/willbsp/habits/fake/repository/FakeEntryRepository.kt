package com.willbsp.habits.fake.repository

import com.willbsp.habits.data.TestData
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

    suspend fun populate2() {
        entries.addAll(TestData.entryListF)
        emit()
    }

    override fun getAllEntriesStream(): Flow<List<Entry>> = observableEntries

    override fun getAllEntriesStream(habitId: Int): Flow<List<Entry>> =
        observableEntries.map { entries -> entries.filter { it.habitId == habitId } }

    override suspend fun getEntry(date: LocalDate, habitId: Int): Entry? =
        entries.find { it.date == date && it.habitId == habitId }

    override suspend fun getOldestEntry(habitId: Int): Entry? =
        entries.filter { it.habitId == habitId }.minByOrNull { it.date }

    override suspend fun toggleEntry(habitId: Int, date: LocalDate) {
        val entry = entries.find { it.habitId == habitId && it.date == date }
        if (entry == null) {
            entries.add(Entry(entries.lastIndex + 1, habitId, date))
        } else {
            entries.remove(entry)
        }
        emit()
    }

    // TODO test this method
    override suspend fun setEntry(habitId: Int, date: LocalDate, completed: Boolean) {
        val entry = entries.find { it.habitId == habitId && it.date == date }
        if (completed && entry == null) {
            entries.add(Entry(entries.lastIndex + 1, habitId, date))
        } else if (!completed && entry != null) {
            entries.remove(entry)
        }
        emit()
    }

}