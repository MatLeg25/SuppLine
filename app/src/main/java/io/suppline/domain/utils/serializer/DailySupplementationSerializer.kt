package io.suppline.domain.utils.serializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import io.suppline.domain.models.DailySupplementation
import io.suppline.domain.models.Supplement
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DailySupplementationSerializer : JsonSerializer<DailySupplementation> {
    override fun serialize(src: DailySupplementation, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val jsonObject = JsonObject()
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE

        jsonObject.addProperty("date", src.date.format(formatter))
        jsonObject.add("supplements", context.serialize(src.supplements))

        return jsonObject
    }
}

class DailySupplementationDeserializer : JsonDeserializer<DailySupplementation> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): DailySupplementation {
        val jsonObject = json.asJsonObject
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE

        val date = LocalDate.parse(jsonObject.get("date").asString, formatter)
        val supplements = context.deserialize<List<Supplement>>(jsonObject.get("supplements"), object : TypeToken<List<Supplement>>() {}.type)

        return DailySupplementation(date, supplements)
    }
}