package io.suppline.data.preferences

import io.suppline.domain.models.Supplement
import io.suppline.domain.preferences.Preferences

class PreferencesFake: Preferences {

    private var dailySupplements: List<Supplement>? = null

    override fun clear() {
        dailySupplements = null
    }

    override fun saveDailySupplements(supplements: List<Supplement>): Boolean {
        dailySupplements = supplements
        return true
    }

    override fun loadDailySupplements(): List<Supplement>? {
        return dailySupplements
    }
}