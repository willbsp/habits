package com.willbsp.habits.fake

import com.willbsp.habits.data.database.HabitEntry
import com.willbsp.habits.data.model.Entry
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.repo.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

class FakeHabitRepository @Inject constructor() : HabitRepository {

    private val habitsStream = MutableSharedFlow<List<Habit>>()
    private suspend fun emit() = habitsStream.emit(FakeDataSource.habitTable)

    override fun getTodaysHabitEntriesStream(): Flow<List<HabitEntry>> {
        TODO("Not yet implemented")
    }

    override suspend fun getEntryForDate(date: String, habitId: Int): Entry? {
        FakeDataSource.entryTable.forEach {
            if (it.date == date && it.habitId == habitId) {
                return it
            }
        }
        return null
    }

    override suspend fun insertHabit(habit: Habit) {
        FakeDataSource.habitTable.add(habit)
        emit()
    }

    override suspend fun insertEntry(entry: Entry) {
        FakeDataSource.entryTable.add(entry)
        emit()
    }

    override suspend fun deleteHabit(habit: Habit) {
        FakeDataSource.habitTable.remove(habit)
        emit()
    }

    override suspend fun deleteEntry(entry: Entry) {
        FakeDataSource.entryTable.remove(entry)
        emit()
    }

    override suspend fun updateHabit(habit: Habit) {
        TODO("Not yet implemented")
        emit()
    }

    override suspend fun updateEntry(entry: Entry) {
        TODO("Not yet implemented")
        emit()
    }

}