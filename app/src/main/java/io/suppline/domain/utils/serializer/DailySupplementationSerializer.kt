package io.suppline.domain.utils.serializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import io.suppline.data.extensions.toDailySupplementation
import io.suppline.data.extensions.toDailySupplementationDb
import io.suppline.data.models.DailySupplementationDb
import io.suppline.data.models.SupplementDb
import io.suppline.domain.models.DailySupplementation
import java.lang.reflect.Type

class DailySupplementationSerializer : JsonSerializer<DailySupplementation> {
    override fun serialize(
        src: DailySupplementation,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        val jsonObject = JsonObject()
        val dailySupplementationDb = src.toDailySupplementationDb()
        jsonObject.addProperty(DailySupplementation::date.name, dailySupplementationDb.date)
        jsonObject.add(
            DailySupplementation::supplements.name,
            context.serialize(dailySupplementationDb.supplements)
        )
        return jsonObject
    }
}

class DailySupplementationDeserializer : JsonDeserializer<DailySupplementation> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): DailySupplementation {
        val jsonObject = json.asJsonObject
        val date = jsonObject.get(DailySupplementation::date.name).asLong
        val supplements = context.deserialize<List<SupplementDb>>(
            jsonObject.get(DailySupplementation::supplements.name),
            object : TypeToken<List<SupplementDb>>() {}.type
        )
        return DailySupplementationDb(date, supplements).toDailySupplementation()
    }
}