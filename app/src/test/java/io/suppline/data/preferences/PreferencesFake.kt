package io.suppline.data.preferences

import io.suppline.domain.models.DailySupplementation
import io.suppline.domain.preferences.Preferences

class PreferencesFake: Preferences {

    private var dailySupplements: DailySupplementation? = null

    override fun clear() {
        dailySupplements = null
    }

    override fun saveDailySupplementation(supplementation: DailySupplementation): Boolean {
        dailySupplements = supplementation
        return true
    }

    override fun loadDailySupplements(): DailySupplementation? {
        return dailySupplements
    }
}