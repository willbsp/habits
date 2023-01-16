package com.willbsp.habits.fake

import com.willbsp.habits.data.model.Entry
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.repo.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow

class FakeOfflineHabitRepository : HabitRepository {

    private val habitsStream = MutableSharedFlow<List<Habit>>()
    suspend fun emit() = habitsStream.emit(FakeDataSource.habitTable)

    override fun getAllHabitsStream(): Flow<List<Habit>> = habitsStream

    override fun entryExistsForDateStream(date: String, habitId: Int): Flow<Boolean> {
        return flow { emit(FakeDataSource.entryExists(date, habitId)) }
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
    }

    override suspend fun insertEntry(entry: Entry) {
        FakeDataSource.entryTable.add(entry)
    }

    override suspend fun deleteHabit(habit: Habit) {
        FakeDataSource.habitTable.remove(habit)
    }

    override suspend fun deleteEntry(entry: Entry) {
        FakeDataSource.entryTable.remove(entry)
    }

    override suspend fun updateHabit(habit: Habit) {
        TODO("Not yet implemented")
    }

    override suspend fun updateEntry(entry: Entry) {
        TODO("Not yet implemented")
    }

}