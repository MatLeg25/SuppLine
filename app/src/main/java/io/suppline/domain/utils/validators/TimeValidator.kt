package io.suppline.domain.utils.validators

import java.time.LocalTime

object TimeValidator {

    fun validateTime(time: LocalTime, hourDelta: Int, minDelta: Int): LocalTime {
        val _hour = time.hour + hourDelta
        val _min = time.minute + minDelta
        val hour = _hour.coerceIn(0..23)
        val min = _min.coerceIn(0..59)
        return LocalTime.of(hour, min)
    }

}