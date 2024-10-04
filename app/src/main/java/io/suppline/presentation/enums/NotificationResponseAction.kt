package io.suppline.presentation.enums

enum class NotificationResponseAction(val value: Int) {
    SNOOZE(0), DONE(1), CANCEL(2);

    companion object {
        fun fromInt(value: Int) = entries.find { it.value == value }
    }
}