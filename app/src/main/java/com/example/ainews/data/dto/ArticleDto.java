package com.example.ainews.data.dto;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import java.lang.reflect.Type;

public class ArticleDto {
    @SerializedName("title") public String title;
    @SerializedName("description") public String description;
    @SerializedName("url") public String url;
    @SerializedName("urlToImage") public String urlToImage;
    @SerializedName("category") public String category;
    @SerializedName("source")
    @JsonAdapter(SourceAdapter.class)
    public SourceDto source;
    @SerializedName("publishedAt") public String publishedAt;

    // Custom deserializer to handle source as string or object
    public static class SourceAdapter implements JsonDeserializer<SourceDto> {
        @Override
        public SourceDto deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonObject()) {
                return context.deserialize(json, SourceDto.class);
            } else if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
                // If source is a string, wrap it in an object
                SourceDto dto = new SourceDto();
                dto.name = json.getAsString();
                return dto;
            }
            return null;
        }
    }
}

