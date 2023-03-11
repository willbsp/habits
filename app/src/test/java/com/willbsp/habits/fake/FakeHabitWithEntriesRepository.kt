package com.willbsp.habits.fake

import com.willbsp.habits.data.model.HabitWithEntries
import com.willbsp.habits.data.repository.EntryRepository
import com.willbsp.habits.data.repository.HabitRepository
import com.willbsp.habits.data.repository.HabitWithEntriesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class FakeHabitWithEntriesRepository(
    private val habitRepository: HabitRepository,
    private val entryRepository: EntryRepository
) : HabitWithEntriesRepository {

    override fun getHabitsWithEntries(): Flow<List<HabitWithEntries>> {
        return combine(
            habitRepository.getAllHabitsStream(),
            entryRepository.getAllEntriesStream()
        ) { habitList, entryList ->
            habitList.map { habit ->
                HabitWithEntries(habit, entryList.filter { it.habitId == habit.id })
            }
        }
    }

}