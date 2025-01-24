package com.example.itp_app.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.beans.property.SimpleStringProperty;

import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private static SessionManager instance;
    private ConcurrentHashMap<String, JsonElement> data;
    private Gson gson;

    private SessionManager() {
        data = new ConcurrentHashMap<>();
        gson = new GsonBuilder()
                .registerTypeAdapter(SimpleStringProperty.class, new SimpleStringPropertyAdapter())
                .create();
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            synchronized (SessionManager.class) {
                if (instance == null) {
                    instance = new SessionManager();
                }
            }
        }
        return instance;
    }

    public void saveResponse(String key, String jsonResponse) {
        JsonElement jsonElement = JsonParser.parseString(jsonResponse);
        data.put(key, jsonElement);
    }

    public <T> void saveObject(String key, T object) {
        JsonElement jsonElement = gson.toJsonTree(object);
        data.put(key, jsonElement);
    }

    public JsonObject getResponse(String key) {
        return data.get(key).getAsJsonObject();
    }

    public <T> T getObject(String key, Class<T> clazz) {
        JsonElement jsonElement = data.get(key);
        return gson.fromJson(jsonElement, clazz);
    }

    public void removeResponse(String key) {
        data.remove(key);
    }

    public void clearAllResponses() {
        data.clear();
    }
}
