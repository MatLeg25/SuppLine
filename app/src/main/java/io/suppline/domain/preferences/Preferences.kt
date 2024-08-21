package io.suppline.domain.preferences

import io.suppline.domain.models.DailySupplementation

interface Preferences {

    companion object {
        const val KEY_DAILY_SUPPLEMENTS = "KEY_DAILY_SUPPLEMENTS"
    }

    fun clear()
    fun saveDailySupplementation(supplementation: DailySupplementation): Boolean
    fun loadDailySupplements(): DailySupplementation?
}