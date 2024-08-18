package io.suppline.presentation

import io.suppline.domain.models.Supplement

data class SuppLineState(
    val dailySupplements: List<Supplement>
)