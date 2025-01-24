package com.example.itp_app.utilities;

import com.google.gson.*;

import javafx.beans.property.SimpleStringProperty;

import java.lang.reflect.Type;

public class SimpleStringPropertyAdapter implements JsonSerializer<SimpleStringProperty>, JsonDeserializer<SimpleStringProperty> {

    @Override
    public JsonElement serialize(SimpleStringProperty src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.get());
    }

    @Override
    public SimpleStringProperty deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new SimpleStringProperty(json.getAsJsonPrimitive().getAsString());
    }
}
