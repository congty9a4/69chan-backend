package com.congty9a4.backend.util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

// Purpose: Custom Gson adapter to read and write OffsetDateTime objects
// Usage: Used in JsonLogging utility class for logging objects with OffsetDateTime fields
public class ODTTypeAdapter implements JsonSerializer<OffsetDateTime>,
        JsonDeserializer<OffsetDateTime> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                "dd/MMM/uuuu HH:mm:ss");

    @Override
    public JsonElement serialize(OffsetDateTime src, Type srcType,
                                 JsonSerializationContext context) {

        return new JsonPrimitive(src.format(formatter));
    }

    @Override
    public OffsetDateTime deserialize(JsonElement json, Type typeOfT,
                                     JsonDeserializationContext context) throws JsonParseException {

        return OffsetDateTime.parse(json.getAsString(), formatter);
    }
}
