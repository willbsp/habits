package com.willbsp.habits.domain.model

import java.time.LocalDate

data class Statistics(
    val total: Int,
    val started: LocalDate?
)
