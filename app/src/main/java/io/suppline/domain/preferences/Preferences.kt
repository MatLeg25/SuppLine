package io.suppline.domain.preferences

import io.suppline.domain.models.Supplement

interface Preferences {

    companion object {
        const val KEY_DAILY_SUPPLEMENTS = "KEY_DAILY_SUPPLEMENTS"
    }

    fun clear()
    fun saveDailySupplements(supplements: List<Supplement>): Boolean
    fun loadDailySupplements(): List<Supplement>?
}