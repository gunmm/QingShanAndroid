package com.qsjt.qingshan.http;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.qsjt.qingshan.model.response.Response;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * @author LiYouGui.
 */

public final class GsonResponseConverter<T> implements Converter<ResponseBody, T> {

    private final Gson gson;

    private final Type type;

    GsonResponseConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(@NonNull ResponseBody value) throws IOException {
        String json = value.string();
        Response response = gson.fromJson(json, Response.class);
        if ("1".equals(response.getResult_code())) {
            return gson.fromJson(json, type);
        }
        return gson.fromJson(gson.toJson(response), type);
    }
}
