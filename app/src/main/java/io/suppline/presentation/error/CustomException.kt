package io.suppline.presentation.error

import io.suppline.presentation.enums.ErrorType

class CustomException(val type: ErrorType, message:String): Exception(message)