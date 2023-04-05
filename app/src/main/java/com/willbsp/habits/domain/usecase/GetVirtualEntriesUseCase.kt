package com.willbsp.habits.domain.usecase

import com.willbsp.habits.data.model.Entry
import com.willbsp.habits.data.model.HabitFrequency
import com.willbsp.habits.data.repository.EntryRepository
import com.willbsp.habits.data.repository.HabitRepository
import com.willbsp.habits.domain.model.VirtualEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

class GetVirtualEntriesUseCase @Inject constructor(
    private val habitRepository: HabitRepository,
    private val entryRepository: EntryRepository
) {

    operator fun invoke(habitId: Int): Flow<List<VirtualEntry>> {

        return combine(
            habitRepository.getHabitStream(habitId),
            entryRepository.getAllEntriesStream(habitId)
        ) { habit, entries ->


            if (entries.isEmpty() || habit == null)
                return@combine listOf<VirtualEntry>()


            when (habit.frequency) {

                HabitFrequency.WEEKLY -> {

                    val virtualEntries = mutableListOf<VirtualEntry>()
                    val completedWeeks = mutableListOf<LocalDate>()

                    var week = entries.first().date.with(DayOfWeek.MONDAY)
                    var count = 0

                    entries.forEach { entry ->
                        virtualEntries.add(entry.toVirtualEntry())
                        val entryWeek = entry.date.with(DayOfWeek.MONDAY)
                        if (entryWeek == week && !completedWeeks.contains(entryWeek)) count++
                        else {
                            week = entryWeek
                            count = 1
                        }
                        if (count >= habit.repeat) completedWeeks.add(entryWeek)
                    }

                    completedWeeks.forEach { weekStart ->
                        (0..6).forEach { offset ->
                            val date = weekStart.plusDays(offset.toLong())
                            if (!virtualEntries.any { it.date == date })
                                virtualEntries.add(VirtualEntry(null, habit.id, date))
                        }
                    }

                    return@combine virtualEntries.sortedByDescending { it.date }
                }

                HabitFrequency.DAILY -> {
                    return@combine entries.map { it.toVirtualEntry() }
                }

            }

        }

    }

    private fun Entry.toVirtualEntry(): VirtualEntry {
        return VirtualEntry(id = id, habitId = habitId, date = date)
    }

}