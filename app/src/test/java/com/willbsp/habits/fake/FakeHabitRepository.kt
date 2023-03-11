package com.willbsp.habits.fake

import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeHabitRepository : HabitRepository {

    val habits = mutableListOf<Habit>()
    private val observableHabits = MutableStateFlow<List<Habit>>(listOf())
    private suspend fun emit() = observableHabits.emit(habits.toList())

    override fun getAllHabitsStream(): Flow<List<Habit>> = observableHabits

    override fun getHabitStream(habitId: Int): Flow<Habit?> {
        return observableHabits.map { it.find { habit -> habit.id == habitId } }
    }

    override suspend fun getHabit(habitId: Int): Habit? {
        return habits.find { it.id == habitId }
    }

    override suspend fun addHabit(habit: Habit) {
        habits.add(Habit(id = habit.id, name = habit.name, frequency = habit.frequency))
        emit()
    }

    override suspend fun updateHabit(habit: Habit) {
        val index = habits.indexOfFirst { it.id == habit.id }
        if (index != -1) habits[index] = habit
        emit()
    }

    override suspend fun deleteHabit(habitId: Int) {
        habits.removeAll { it.id == habitId }
        emit()
    }

}