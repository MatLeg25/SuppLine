package io.suppline.domain.models

import java.time.LocalDate

data class DailySupplementation(
    val date: LocalDate,
    val supplements: List<Supplement>
)