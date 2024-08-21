package io.suppline.presentation

import io.suppline.domain.models.Supplement
import java.time.LocalDate

data class SuppLineState(
    val date: LocalDate = LocalDate.now(),
    val supplementsMap: Map<Supplement, Boolean> = emptyMap(),
    val progress: Float = 0f,
)