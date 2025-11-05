package com.example.ainews.data.dto;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class InitResponse {
    @SerializedName("articles") public List<ArticleDto> articles;
    @SerializedName("forcedDiversify") public Boolean forcedDiversify;
}

