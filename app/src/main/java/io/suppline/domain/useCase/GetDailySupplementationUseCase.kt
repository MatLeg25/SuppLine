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
        return preferences.loadDailySupplements() ?: kotlin.run {
            DailySupplementation(
                date = LocalDate.now().plusDays(9),
                supplements = Config.DEFAULT_SUPPLEMENT_NAMES.mapIndexed { index, s ->
                    Supplement(
                        id = index,
                        name = s,
                        consumed = false,
                        timeToConsume = LocalTime.now(),
                        hasNotification = false
                    )
                }
            )
        }
    }
}

