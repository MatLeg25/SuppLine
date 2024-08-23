package io.suppline.domain.models

import java.time.LocalTime

data class Supplement(
    val id: Int,
    val name: String,
    val consumed: Boolean,
    val timeToConsume: LocalTime,
)