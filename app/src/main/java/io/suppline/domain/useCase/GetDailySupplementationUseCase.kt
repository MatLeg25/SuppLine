package io.suppline.domain.useCase

import io.suppline.domain.models.DailySupplementation
import io.suppline.domain.preferences.Preferences
import java.time.LocalDate

class GetDailySupplementationUseCase(
    private val preferences: Preferences
) {

    operator fun invoke(): DailySupplementation? {
        val savedSupplementation = preferences.loadDailySupplements() ?: return null
        return getForToday(savedSupplementation)
    }

    private fun getForToday(supplementation: DailySupplementation): DailySupplementation {
        val today = LocalDate.now()
        val isToday = supplementation.date.isEqual(today)
        return if (isToday) supplementation else {
            supplementation.copy(
                date = today,
                supplements = supplementation.supplements.map { supplement ->
                    supplement.copy(consumed = false)
                }
            )
        }
    }

}

