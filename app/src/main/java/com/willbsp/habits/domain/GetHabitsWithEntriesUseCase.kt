package com.willbsp.habits.domain

import com.willbsp.habits.data.repo.EntryRepository
import com.willbsp.habits.data.repo.HabitRepository
import com.willbsp.habits.domain.model.HabitWithEntries
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import javax.inject.Inject

class GetHabitsWithEntriesUseCase @Inject constructor(
    private val habitRepository: HabitRepository,
    private val entryRepository: EntryRepository
) {

    operator fun invoke(): Flow<List<HabitWithEntries>> {
        return habitRepository.getAllHabits()
            .combine(entryRepository.getAllEntries()) { habitList, entryList ->
                habitList.map { habit ->
                    HabitWithEntries(habit, entryList.filter { it.habitId == habit.id })
                }
            }
    }

    operator fun invoke(date: LocalDate): Flow<List<HabitWithEntries>> {
        return habitRepository.getAllHabits()
            .combine(entryRepository.getAllEntries()) { habitList, entryList ->
                habitList.map { habit ->
                    HabitWithEntries(
                        habit,
                        entryList.filter { it.habitId == habit.id && it.date == date })
                }
            }
    }

}