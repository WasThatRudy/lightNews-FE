package com.example.ainews.data.dto;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SwipeResponse {
    @SerializedName("articles") public List<ArticleDto> articles;
}

