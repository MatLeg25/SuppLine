package io.suppline.presentation.mapper

import io.suppline.presentation.models.Notification
import io.suppline.presentation.models.ParcelableNotification

fun Notification.toParcelable() = ParcelableNotification(
    id = this.id,
    name = this.name,
    timeInMillis = this.timeInMillis,
    active = this.active,
    isDaily = this.isDaily
)

fun ParcelableNotification.toDomainModel() = Notification(
    id = this.id,
    name = this.name,
    timeInMillis = this.timeInMillis,
    active = this.active,
    isDaily = this.isDaily
)