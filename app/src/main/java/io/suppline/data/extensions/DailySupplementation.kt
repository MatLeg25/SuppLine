package io.suppline.data.extensions

import io.suppline.data.models.DailySupplementationDb
import io.suppline.domain.models.DailySupplementation
import java.time.LocalDate

fun DailySupplementation.toDailySupplementationDb() = DailySupplementationDb(
    date = this.date.toEpochDay(),
    supplements = this.supplements.map { it.toSupplementDb() }
)

fun DailySupplementationDb.toDailySupplementation() = DailySupplementation(
    date = LocalDate.ofEpochDay(this.date),
    supplements = this.supplements.map { it.toSupplement() }
)