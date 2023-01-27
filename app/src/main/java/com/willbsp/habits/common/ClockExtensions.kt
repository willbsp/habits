package com.willbsp.habits.common

import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun Clock.getCurrentFormattedDate(): String {

    val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
    return LocalDateTime.now(this).format(formatter)

}

fun Clock.getPreviousDatesList(days: Int): List<LocalDate> {

    val clock = this

    return buildList {
        repeat(days) { index ->
            this.add(LocalDate.now(clock).minusDays(index.toLong()))
        }
    }

}