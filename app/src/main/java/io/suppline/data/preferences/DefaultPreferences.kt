package io.suppline.data.preferences
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.suppline.domain.models.DailySupplementation
import io.suppline.domain.preferences.Preferences
import io.suppline.domain.utils.serializer.DailySupplementationDeserializer
import io.suppline.domain.utils.serializer.DailySupplementationSerializer


class DefaultPreferences(
    private val sharedPref: SharedPreferences
): Preferences {

    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(DailySupplementation::class.java, DailySupplementationSerializer())
        .registerTypeAdapter(DailySupplementation::class.java, DailySupplementationDeserializer())
        .create()


    override fun clear() {
        sharedPref.edit().clear().apply()
    }

    override fun saveDailySupplementation(supplementation: DailySupplementation): Boolean {
        val supplementationJson = gson.toJson(supplementation)
        return if (!supplementationJson.isNullOrBlank()) {
            sharedPref.edit()
                .putString(Preferences.KEY_DAILY_SUPPLEMENTS, supplementationJson)
                .apply()
            true
        } else false
    }

    override fun loadDailySupplements(): DailySupplementation? {
        val supplementationJson = sharedPref.getString(Preferences.KEY_DAILY_SUPPLEMENTS, null)
        return if (supplementationJson != null) {
            gson.fromJson(supplementationJson, DailySupplementation::class.java)
        } else {
            null
        }
    }
}