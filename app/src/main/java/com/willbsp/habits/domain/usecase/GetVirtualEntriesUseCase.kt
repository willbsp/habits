package com.willbsp.habits.domain.usecase

import com.willbsp.habits.data.model.Entry
import com.willbsp.habits.data.model.HabitFrequency
import com.willbsp.habits.data.repository.EntryRepository
import com.willbsp.habits.data.repository.HabitRepository
import com.willbsp.habits.domain.model.VirtualEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.DayOfWeek
import javax.inject.Inject

/**
 * Fill in missing dates for score and streak calculation
 * Returns filled dates for weekly habits else normal dates for daily habits
 */
class GetVirtualEntriesUseCase @Inject constructor(
    val entryRepository: EntryRepository,
    val habitRepository: HabitRepository
) {

    operator fun invoke(habitId: Int): Flow<List<VirtualEntry>> {

        return combine(
            habitRepository.getHabitStream(habitId),
            entryRepository.getAllEntriesStream(habitId)
        ) { habit, entries ->

            if (habit == null)
                return@combine listOf<VirtualEntry>()

            if (habit.frequency == HabitFrequency.WEEKLY) {

                val virtualEntries = mutableListOf<VirtualEntry>()
                var week = entries.first().date.with(DayOfWeek.MONDAY)
                var count = 0

                entries.forEach { entry ->
                    println("$entry")
                    virtualEntries.add(entry.toVirtualEntry())
                    if (entry.date.with(DayOfWeek.MONDAY) == week)
                        count++
                    else {
                        week = entry.date.with(DayOfWeek.MONDAY)
                        count = 1
                    }
                    if (count >= habit.repeat) {
                        repeat(7) { offset ->
                            val date = week.plusDays(offset.toLong())
                            if (!entries.any { it.date == date })
                                virtualEntries.add(VirtualEntry(null, habitId, date))
                        }
                    }
                }

                return@combine virtualEntries.sortedByDescending { it.date }

            } else
                return@combine entries.map { it.toVirtualEntry() }.sortedByDescending { it.date }

        }

    }

    private fun Entry.toVirtualEntry(): VirtualEntry {
        return VirtualEntry(id = id, habitId = habitId, date = date)
    }


}