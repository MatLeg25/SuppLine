package io.suppline.data

import io.suppline.domain.models.DailySupplementation
import io.suppline.domain.preferences.Preferences
import java.time.LocalDate

class FakePreferences: Preferences {

    private val dailySupplementationMap: MutableMap<Long, DailySupplementation> = mutableMapOf()
    private val lock = Any()

    override fun clear() {
        synchronized(lock) {
            dailySupplementationMap.clear()
        }
    }

    override fun saveDailySupplementation(supplementation: DailySupplementation): Boolean {
        synchronized(lock) {
            val todayMillis = LocalDate.now().toEpochDay()
            dailySupplementationMap[todayMillis] = supplementation
            return true
        }
    }

    override fun loadDailySupplements(): DailySupplementation? {
        synchronized(lock) {
            val todayMillis = LocalDate.now().toEpochDay()
            return dailySupplementationMap[todayMillis]?.copy()
        }
    }

}