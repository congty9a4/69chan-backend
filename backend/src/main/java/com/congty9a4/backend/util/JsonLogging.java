package com.congty9a4.backend.util;

import com.google.gson.*;

import java.time.OffsetDateTime;

// Purpose: Utility class for converting objects to JSON strings for logging purposes
public final class JsonLogging {
    public static <T> String toString(T object){
    // Source - https://stackoverflow.com/a
    // Posted by Praveen Kumar Verma, modified by community. See post 'Timeline' for change history
    // Retrieved 2025-12-29, License - CC BY-SA 4.0
        Gson gson = new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(OffsetDateTime.class, new ODTTypeAdapter())
                .create();
        return gson.toJson(object);
    }
}
