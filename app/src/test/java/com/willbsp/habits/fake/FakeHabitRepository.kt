package com.willbsp.habits.fake

import com.willbsp.habits.data.model.Entry
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.model.HabitEntry
import com.willbsp.habits.data.repo.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeHabitRepository : HabitRepository {

    val habits = mutableListOf<Habit>()
    val entries = mutableListOf<Entry>()

    private var observableHabitEntries = MutableStateFlow<List<HabitEntry>>(listOf())
    private var entryStreamDate = ""

    override fun getHabitEntriesForDateStream(date: String): Flow<List<HabitEntry>> {
        entryStreamDate = date
        return observableHabitEntries
    }

    override suspend fun getHabitById(id: Int): Habit {
        TODO("Not yet implemented")
    }

    override suspend fun getEntryForDate(date: String, habitId: Int): Entry? {
        entries.forEach {
            if (it.habitId == habitId && it.date == date)
                return it
        }
        return null
    }

    override suspend fun addHabit(habit: Habit) {
        habits.add(habit)
        emit()
    }

    override suspend fun updateHabit(habit: Habit) {
        TODO("Not yet implemented")
    }

    override suspend fun toggleEntry(habitId: Int, date: String) {
        val entry: Entry? = getEntryForDate(habitId = habitId, date = date)
        val index = entries.lastIndex
        if (entry == null) entries.add(Entry(id = index, habitId = habitId, date = date))
        else entries.remove(entry)
        emit()
    }

    override suspend fun deleteHabit(habit: Habit) {
        habits.remove(habit)
        emit()
    }

    /**
     * Call after every change to the data to imitate flows emitting when database is changed
     */
    private suspend fun emit() {
        observableHabitEntries.emit(getHabitEntriesForDate(entryStreamDate))
    }

    /**
     * Map the list of habits and list of entries to habit entries
     */
    private fun getHabitEntriesForDate(date: String): List<HabitEntry> {
        return habits.map { habit ->
            var completed = false
            entries.forEach { entry ->
                if (entry.habitId == habit.id && entry.date == date)
                    completed = true
            }
            HabitEntry(habit.id, habit.name, completed)
        }
    }

}