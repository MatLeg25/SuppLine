package io.suppline.presentation.models

data class Notification(
    val id: Int,
    val name: String,
    val timeInMillis: Long,
    val active: Boolean,
    val isDaily: Boolean
)