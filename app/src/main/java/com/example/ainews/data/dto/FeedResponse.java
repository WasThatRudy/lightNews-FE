package com.example.ainews.data.dto;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class FeedResponse {
    @SerializedName("articles") public List<ArticleDto> articles;

    public static class CategoriesResponse {
        @SerializedName("categories") public List<String> categories;
    }
}

