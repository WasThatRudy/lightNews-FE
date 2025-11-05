package com.example.ainews.data.dto;

import com.google.gson.annotations.SerializedName;

public class SwipeEvent {
    @SerializedName("category") public String category;
    @SerializedName("articleUrl") public String articleUrl;
    @SerializedName("reaction") public String reaction; // like/dislike
}

