package com.willbsp.habits

import com.willbsp.habits.data.model.Entry
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.model.HabitFrequency
import java.time.LocalDate

object TestData {

    val habit1 = Habit(id = 1, name = "Running", frequency = HabitFrequency.DAILY)
    val habit2 = Habit(id = 2, name = "Reading", HabitFrequency.WEEKLY)

    val entry1: Entry = Entry(1, 1, LocalDate.parse("2023-01-13"))
    val entry2: Entry = Entry(2, 2, LocalDate.parse("2023-04-15"))

    // consecutive entries for testing streak calculation
    val entryList: List<Entry> = listOf(
        entry1,
        entry2,
        Entry(3, 2, LocalDate.parse("2023-07-15")),
        Entry(4, 2, LocalDate.parse("2023-07-16")),
        Entry(5, 2, LocalDate.parse("2023-07-17")),
        Entry(6, 2, LocalDate.parse("2023-07-18")),
        Entry(7, 2, LocalDate.parse("2023-07-19"))
    )

}