package com.willbsp.habits.fake

import com.willbsp.habits.data.model.Entry
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.model.HabitFrequency

object FakeDataSource {

    val habit1 = Habit(1, "Running", HabitFrequency.DAILY)
    val entry1H1 = Entry(1, 1, "2023-01-16")
    val entry2H1 = Entry(2, 1, "2023-02-14")

    val habit2 = Habit(2, "Walking", HabitFrequency.WEEKLY)
    val entry1H2 = Entry(3, 2, "2023-04-13")
    val entry2H2 = Entry(4, 2, "2023-03-27")

    var habitTable = mutableListOf(
        habit1,
        habit2
    )

    var entryTable = mutableListOf(
        entry1H1,
        entry2H1,
        entry1H2,
        entry2H2
    )

    fun entryExists(date: String, habitId: Int): Boolean {
        entryTable.forEach {
            if (it.habitId == habitId && it.date == date) {
                return true
            }
        }
        return false
    }

}