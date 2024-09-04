package io.suppline.presentation.states

import io.suppline.domain.models.Supplement
import java.time.LocalDate

data class SuppLineState(
    val date: LocalDate = LocalDate.now(),
    val supplements: List<Supplement> = emptyList(),
    val progress: Float = 0f,
    val configItemIndex: Int? = null,
    val groupSectionsByTime: Boolean = false,
    val editedItem: Supplement? = null
) {
    fun getSupplementsByHours(): Map<Int, List<Supplement>> {
        return supplements.groupBy {
            it.timeToConsume.hour
        }
    }
}