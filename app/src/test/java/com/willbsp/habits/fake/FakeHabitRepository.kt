package com.willbsp.habits.fake

import com.willbsp.habits.data.model.Entry
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.model.HabitWithEntries
import com.willbsp.habits.data.repo.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate

class FakeHabitRepository : HabitRepository {

    val habits = mutableListOf<Habit>()
    private val entries = mutableListOf<Entry>()

    private var observableHabitEntries = MutableStateFlow<List<HabitWithEntries>>(listOf())
    private var dates: List<LocalDate> = listOf()

    override fun getAllHabitsWithEntriesForDates(dates: List<LocalDate>): Flow<List<HabitWithEntries>> {
        this.dates = dates
        return observableHabitEntries
    }

    override suspend fun getHabitById(habitId: Int): Habit {
        return habits.first {
            it.id == habitId
        }
    }

    override suspend fun getEntryForDate(date: LocalDate, habitId: Int): Entry? {
        return try {
            entries.first {
                it.habitId == habitId && it.date == date
            }
        } catch (e: NoSuchElementException) {
            null
        }
    }

    override suspend fun addHabit(habit: Habit) {
        habits.add(habit)
        emit()
    }

    override suspend fun updateHabit(habit: Habit) {
        TODO("Not yet implemented")
    }

    override suspend fun toggleEntry(habitId: Int, date: LocalDate) {
        val entry: Entry? = getEntryForDate(habitId = habitId, date = date)
        val index = entries.lastIndex
        if (entry == null) entries.add(Entry(id = index, habitId = habitId, date = date))
        else entries.remove(entry)
        emit()
    }

    override suspend fun deleteHabit(habitId: Int) {
        habits.remove(getHabitById(habitId))
        emit()
    }

    /**
     * Call after every change to the data to imitate flows emitting when database is changed
     */
    private suspend fun emit() {
        observableHabitEntries.emit(getHabitEntriesForDates(dates))
    }

    /**
     * Map the list of habits and list of entries to habit entries
     */
    private fun getHabitEntriesForDates(dates: List<LocalDate>): List<HabitWithEntries> {
        return habits.map { habit ->
            val entriesForHabit = entries.filter { entry ->
                entry.habitId == habit.id && dates.any { entry.date == it }
            }
            HabitWithEntries(habit, entriesForHabit.requireNoNulls())
        }
    }

}