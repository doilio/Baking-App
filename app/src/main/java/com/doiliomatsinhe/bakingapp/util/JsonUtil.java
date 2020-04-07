package com.doiliomatsinhe.bakingapp.util;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class JsonUtil {
    public static String getJsonFromObject(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static Object getObjectFromJson(String json, Type type) {
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }
}