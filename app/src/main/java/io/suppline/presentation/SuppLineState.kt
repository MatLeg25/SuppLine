package io.suppline.presentation

import io.suppline.domain.models.Supplement
import java.time.LocalDate

data class SuppLineState(
    val date: LocalDate = LocalDate.now(),
    val supplements: List<Supplement> = emptyList(),
    val progress: Float = 0f,
    val editedItemIndex: Int? = null,
    val groupSectionsByTime: Boolean = false
) {
    fun getSupplementsByHours(): Map<Int, List<Supplement>> {
        return supplements.groupBy {
            it.timeToConsume.hour
        }
    }
}