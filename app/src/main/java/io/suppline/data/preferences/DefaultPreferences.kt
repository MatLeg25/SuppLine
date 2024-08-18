package io.suppline.data.preferences
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.suppline.domain.models.Supplement
import io.suppline.domain.preferences.Preferences


class DefaultPreferences(
    private val sharedPref: SharedPreferences
): Preferences {

    private val gson = Gson()

    override fun clear() {
        sharedPref.edit().clear().apply()
    }

    override fun saveDailySupplements(supplements: List<Supplement>): Boolean {
        val supplementsJson = gson.toJson(supplements)

        return if (!supplementsJson.isNullOrBlank()) {
            sharedPref.edit()
                .putString(Preferences.KEY_DAILY_SUPPLEMENTS, supplements.toString())
                .apply()
            true
        } else false
    }

    override fun loadDailySupplements(): List<Supplement>? {
        val listType = object : TypeToken<List<Supplement>>() {}
        val supplementsJson = sharedPref.getString(Preferences.KEY_DAILY_SUPPLEMENTS, null)

        return if (supplementsJson != null) {
            gson.fromJson(supplementsJson, listType)
        } else {
            null
        }
    }
}