package io.suppline.data.models

data class SupplementDb(
    val id: Int,
    val name: String,
    val description: String,
    val consumed: Boolean,
    val timeToConsume: Int,
    val hasNotification: Boolean
)