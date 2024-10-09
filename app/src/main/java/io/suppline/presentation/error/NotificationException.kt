package io.suppline.presentation.error

import io.suppline.presentation.enums.ErrorType

class NotificationException(val type: ErrorType, message:String): Exception(message)