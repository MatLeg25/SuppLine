package io.suppline.domain.useCase

import io.suppline.domain.Config
import io.suppline.domain.models.DailySupplementation
import io.suppline.domain.models.Supplement
import io.suppline.domain.preferences.Preferences
import java.time.LocalDate
import java.time.LocalTime

class GetDailySupplementationUseCase(
    private val preferences: Preferences
) {

    operator fun invoke(): DailySupplementation {
        val savedSupplementation = preferences.loadDailySupplements()
        return if (savedSupplementation == null) getDefaultDailySupplementation()
        else getForToday(savedSupplementation)
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

    private fun getDefaultDailySupplementation(): DailySupplementation {
        return DailySupplementation(
            date = LocalDate.now(),
            supplements = Config.DEFAULT_SUPPLEMENT_NAMES.mapIndexed { index, s ->
                Supplement(
                    id = index,
                    name = s,
                    description = "",
                    consumed = false,
                    timeToConsume = LocalTime.now(),
                    hasNotification = false
                )
            }
        )
    }

}

