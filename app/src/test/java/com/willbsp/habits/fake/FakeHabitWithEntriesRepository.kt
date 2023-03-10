package com.willbsp.habits.fake

import com.willbsp.habits.data.model.HabitWithEntries
import com.willbsp.habits.data.repository.HabitRepository
import com.willbsp.habits.data.repository.HabitWithEntriesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class FakeHabitWithEntriesRepository(
    private val fakeHabitRepository: HabitRepository,
    private val fakeEntryRepository: FakeEntryRepository
) : HabitWithEntriesRepository {

    override fun getHabitsWithEntries(): Flow<List<HabitWithEntries>> {
        return combine(
            fakeHabitRepository.getAllHabitsStream(),
            fakeEntryRepository.getAllEntriesStream()
        ) { habitList, entryList ->
            habitList.map { habit ->
                HabitWithEntries(habit, entryList.filter { it.habitId == habit.id })
            }
        }
    }

}