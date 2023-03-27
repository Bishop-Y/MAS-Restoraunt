package org.cafe.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class JsonParser {
    private final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) -> {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return LocalDateTime.parse(json.getAsJsonPrimitive().getAsString(), formatter);
    }).create();

    public <T> T parse(String filePath, Class<T> type) throws IOException {
        Reader reader = Files.newBufferedReader(Paths.get(filePath));
        var model = gson.fromJson(reader, TypeToken.get(type));
        return Optional.of(model).get();
    }
}

