package io.suppline.domain.models

import java.time.LocalTime

data class Supplement(
    val id: Int,
    val name: String,
    val description: String,
    val consumed: Boolean,
    val timeToConsume: LocalTime,
    val hasNotification: Boolean
)