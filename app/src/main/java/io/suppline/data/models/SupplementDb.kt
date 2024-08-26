package io.suppline.data.models

data class SupplementDb(
    val id: Int,
    val name: String,
    val consumed: Boolean,
    val timeToConsume: Int,
)