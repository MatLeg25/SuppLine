package io.suppline.domain.useCase

import io.suppline.domain.models.DailySupplementation
import io.suppline.domain.preferences.Preferences

class SaveDailySupplementationUseCase(
    private val preferences: Preferences
) {
    operator fun invoke(supplementation: DailySupplementation) {
        preferences.saveDailySupplementation(supplementation)
    }
}