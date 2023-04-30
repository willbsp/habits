package com.willbsp.habits.domain.usecase

import com.willbsp.habits.data.repository.HabitRepository
import com.willbsp.habits.domain.model.HabitWithVirtualEntries
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Fill in missing dates for score and streak calculation
 * Returns filled dates for weekly habits else normal dates for daily habits
 */
class GetHabitsWithVirtualEntriesUseCase @Inject constructor(
    val habitRepository: HabitRepository,
    val getVirtualEntries: GetVirtualEntriesUseCase
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<List<HabitWithVirtualEntries>> {

        return habitRepository.getAllHabitsStream().flatMapLatest { habitList ->

            val flows = habitList.map { habit ->
                return@map getVirtualEntries(habit.id).map { virtualEntries ->
                    HabitWithVirtualEntries(habit, virtualEntries)
                }
            }

            if (flows.isEmpty())
                return@flatMapLatest flowOf(listOf<HabitWithVirtualEntries>())

            return@flatMapLatest combine(flows) { it.toList() }

        }

    }

}