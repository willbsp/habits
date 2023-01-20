package com.willbsp.habits

import com.willbsp.habits.data.model.Entry
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.model.HabitFrequency

object TestData {

    val habit1 = Habit(id = 1, name = "Running", frequency = HabitFrequency.DAILY)
    val habit2 = Habit(id = 2, name = "Reading", HabitFrequency.WEEKLY)

    val entry1: Entry = Entry(1, 1, "2023-01-13")
    val entry2: Entry = Entry(2, 2, "2023-04-15")

}