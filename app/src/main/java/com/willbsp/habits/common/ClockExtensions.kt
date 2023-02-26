package com.willbsp.habits.common

import java.time.Clock
import java.time.LocalDate

fun Clock.getPreviousDates(days: Int): List<LocalDate> {

    val clock = this

    return buildList {
        repeat(days) { index ->
            this.add(LocalDate.now(clock).minusDays(index.toLong()))
        }
    }

}