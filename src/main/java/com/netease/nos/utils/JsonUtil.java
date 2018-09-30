package com.netease.nos.utils;

import com.google.gson.Gson;

import java.util.Objects;

/**
 * Created by future on 2018/9/30
 */
public class JsonUtil {
    private static final Gson gson = new Gson();

    public static String toJsonStr(Object object) {
        Objects.requireNonNull(object);
        return gson.toJson(object);
    }
}
