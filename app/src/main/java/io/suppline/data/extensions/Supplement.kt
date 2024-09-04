package io.suppline.data.extensions

import io.suppline.data.models.SupplementDb
import io.suppline.domain.models.Supplement
import java.time.LocalTime

fun Supplement.toSupplementDb() = SupplementDb(
    id = this.id,
    name = this.name,
    description = this.description,
    consumed = this.consumed,
    timeToConsume = this.timeToConsume.toSecondOfDay(),
    hasNotification = this.hasNotification
)

fun SupplementDb.toSupplement() = Supplement(
    id = this.id,
    name = this.name,
    description = this.description,
    consumed = this.consumed,
    timeToConsume = LocalTime.ofSecondOfDay(this.timeToConsume.toLong()),
    hasNotification = this.hasNotification
)