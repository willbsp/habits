package com.willbsp.habits.domain.usecase

import com.willbsp.habits.data.model.Entry
import com.willbsp.habits.data.model.HabitFrequency
import com.willbsp.habits.data.model.HabitWithEntries
import com.willbsp.habits.data.repository.HabitWithEntriesRepository
import com.willbsp.habits.domain.model.HabitWithVirtualEntries
import com.willbsp.habits.domain.model.VirtualEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

/**
 * Fill in missing dates for score and streak calculation
 * Returns filled dates for weekly habits else normal dates for daily habits
 */
class GetHabitsWithVirtualEntriesUseCase @Inject constructor(
    val habitRepository: HabitWithEntriesRepository
) {

    operator fun invoke(): Flow<List<HabitWithVirtualEntries>> {

        return habitRepository.getHabitsWithEntries().map invokeMap@{ habitsWithEntries ->

            if (habitsWithEntries.isEmpty())
                return@invokeMap listOf<HabitWithVirtualEntries>()

            return@invokeMap habitsWithEntries.map { habitWithEntries ->

                val habit = habitWithEntries.habit
                val entries = habitWithEntries.entries.sortedByDescending { it.date }

                if (entries.isEmpty())
                    return@map HabitWithVirtualEntries(habit, listOf())

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

                        return@map HabitWithVirtualEntries(
                            habit,
                            virtualEntries.sortedByDescending { it.date }
                        )
                    }

                    HabitFrequency.DAILY -> {
                        habitWithEntries.toHabitWithVirtualEntries()
                    }

                }

            }

        }

    }

    private fun HabitWithEntries.toHabitWithVirtualEntries(): HabitWithVirtualEntries {
        return HabitWithVirtualEntries(
            this.habit,
            this.entries.map { it.toVirtualEntry() }.sortedByDescending { it.date })
    }

    private fun Entry.toVirtualEntry(): VirtualEntry {
        return VirtualEntry(id = id, habitId = habitId, date = date)
    }


}