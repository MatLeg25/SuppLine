package io.suppline.domain.utils.extensions

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

fun LocalTime.localTimeToEpochMillis(): Long {
    val today = LocalDate.now()
    val zonedDateTime = ZonedDateTime.of(today, this, ZoneId.systemDefault())
    return zonedDateTime.toInstant().toEpochMilli()
}