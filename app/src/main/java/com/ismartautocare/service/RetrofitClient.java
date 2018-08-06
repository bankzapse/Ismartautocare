package com.ismartautocare.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by programmer on 1/10/18.
 */

public class RetrofitClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl) {
        if (retrofit==null) {
            Gson gson = new GsonBuilder().serializeNulls().excludeFieldsWithoutExposeAnnotation().create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static class StringConverter implements JsonSerializer<String>,
            JsonDeserializer<String> {
        public JsonElement serialize(String src, Type typeOfSrc,
                                     JsonSerializationContext context) {
            if ( src == null ) {
                return new JsonPrimitive("");
            } else {
                return new JsonPrimitive(src.toString());
            }
        }
        public String deserialize(JsonElement json, Type typeOfT,
                                  JsonDeserializationContext context)
                throws JsonParseException {
            return json.getAsJsonPrimitive().getAsString();
        }
    }

//    public static class NullStringToEmptyAdapterFactory<T> implements TypeAdapterFactory {
//        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
//
//            Class<T> rawType = (Class<T>) type.getRawType();
//            if (rawType != String.class) {
//                return null;
//            }
//            return (TypeAdapter<T>) new StringAdapter();
//        }
//    }
//
//    public static class StringAdapter extends TypeAdapter<String> {
//        public String read(JsonReader reader) throws IOException {
//            if (reader.peek() == JsonToken.NULL) {
//                reader.nextNull();
//                return "";
//            }
//            return reader.nextString();
//        }
//        public void write(JsonWriter writer, String value) throws IOException {
//            if (value == null) {
//                writer.nullValue();
//                return;
//            }
//            writer.value(value);
//        }
//    }
}