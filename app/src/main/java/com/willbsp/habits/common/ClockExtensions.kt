package com.willbsp.habits.common

import java.time.Clock
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun Clock.getCurrentFormattedDate(): String {

    val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
    return LocalDateTime.now(this).format(formatter)

}

fun Clock.getPreviousDaysFormattedDate(days: Int): List<String> {

    val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
    val clock = this

    return buildList {
        repeat(days) { index ->
            this.add(LocalDateTime.now(clock).minusDays(index.toLong()).format(formatter))
        }
    }

}