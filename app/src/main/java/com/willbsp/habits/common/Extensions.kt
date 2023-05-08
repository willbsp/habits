package com.willbsp.habits.common

import java.time.LocalDate

operator fun LocalDate.rangeTo(other: LocalDate) = DateProgression(this, other)