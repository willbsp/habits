package com.willbsp.habits.common

import java.time.Clock
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun Clock.getCurrentFormattedDate(): String {

    val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT) // TODO make constant
    return LocalDateTime.now(this).format(formatter)

}