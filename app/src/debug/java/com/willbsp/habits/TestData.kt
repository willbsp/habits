package com.willbsp.habits

import com.willbsp.habits.data.model.Entry
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.data.model.HabitFrequency
import java.time.LocalDate

object TestData {

    val habit1 = Habit(id = 0, name = "Running", frequency = HabitFrequency.DAILY)
    val habit2 = Habit(id = 1, name = "Reading", HabitFrequency.WEEKLY)
    val habit3 = Habit(id = 2, name = "Flashcards", HabitFrequency.DAILY)

    val entry1: Entry = Entry(1, 1, LocalDate.parse("2023-01-13"))
    val entry2: Entry = Entry(2, 2, LocalDate.parse("2023-04-15"))

    // entries for testing domain layer
    // streak (from 07-20): 5
    // score: ~37%
    val entryList: List<Entry> = listOf(
        Entry(7, 2, LocalDate.parse("2023-02-11")),
        Entry(8, 2, LocalDate.parse("2023-02-13")),
        Entry(9, 2, LocalDate.parse("2023-02-23")),
        Entry(10, 2, LocalDate.parse("2023-02-27")),
        Entry(11, 2, LocalDate.parse("2023-03-01")),
        Entry(12, 2, LocalDate.parse("2023-03-03")),
        Entry(13, 2, LocalDate.parse("2023-03-05")),
        Entry(14, 2, LocalDate.parse("2023-03-06")),
        Entry(15, 2, LocalDate.parse("2023-03-07")),
        Entry(16, 2, LocalDate.parse("2023-03-08")),
        Entry(17, 2, LocalDate.parse("2023-03-09"))
    )

}