package io.suppline.presentation

import io.suppline.domain.models.Supplement

data class SuppLineState(
    val supplementsMap: Map<Supplement, Boolean> = emptyMap(),
    val progress: Float = 0f,
)