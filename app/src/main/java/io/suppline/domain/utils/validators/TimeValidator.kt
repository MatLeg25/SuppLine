package io.suppline.domain.utils.validators

import java.time.LocalTime

object TimeValidator {
    fun validateTime(hour: Int, min: Int): LocalTime {
        val h = hour.coerceIn(0..23)
        val m = min.coerceIn(0..59)
        return LocalTime.of(h, m)
    }
}