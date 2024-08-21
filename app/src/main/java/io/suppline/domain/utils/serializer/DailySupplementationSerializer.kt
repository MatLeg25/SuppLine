package io.suppline.domain.utils.serializer

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import io.suppline.domain.models.DailySupplementation
import io.suppline.domain.models.Supplement
import java.lang.reflect.Type
import java.time.LocalDate

private val gsonLocalDate: Gson = GsonBuilder()
    .registerTypeAdapter(LocalDate::class.java, LocalDateSerializer())
    .registerTypeAdapter(LocalDate::class.java, LocalDateDeserializer())
    .create()


class DailySupplementationSerializer : JsonSerializer<DailySupplementation> {
    override fun serialize(
        src: DailySupplementation?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        val jsonObject = JsonObject()
        val gson = Gson()
        //LocalDate
        val jsonLocalDate = gsonLocalDate.toJson(src!!.date)
        jsonObject.add(DailySupplementation::date.name, gson.toJsonTree(jsonLocalDate)!!)
        //Supplements
        val jsonArray = JsonArray()
        src.supplements.forEach { (supplement, b) ->
            jsonArray.add(
                JsonObject().apply {
                    addProperty(Supplement::name.name, supplement.name)
                    addProperty(Supplement::id.name, supplement.id)
                    addProperty("consumed", b)
                }
            )
        }
        jsonObject.add(DailySupplementation::supplements.name, jsonArray)

        return jsonObject
    }
}

class DailySupplementationDeserializer : JsonDeserializer<DailySupplementation> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): DailySupplementation {
        val jsonObject = json?.asJsonObject ?: JsonObject()
        //LocalDate
        val localDateJson = jsonObject[DailySupplementation::date.name]!!.asString
        val localDate = gsonLocalDate.fromJson(localDateJson, LocalDate::class.java)
        //Supplements
        val supplementsJsonArray = jsonObject[DailySupplementation::supplements.name]!!.asJsonArray
        val supplementsMap = mutableMapOf<Supplement, Boolean>()
        supplementsJsonArray.forEach {
            val item = it.asJsonObject
            val supplement = Supplement(
                id = item.get(Supplement::id.name).asInt,
                name = item.get(Supplement::name.name).asString
            )
            supplementsMap[supplement] = item.get("consumed").asBoolean
        }

        return DailySupplementation(
            date = localDate,
            supplements = supplementsMap
        )
    }
}