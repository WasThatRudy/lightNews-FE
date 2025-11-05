package com.example.ainews.data.dto;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SwipeRequest {
    @SerializedName("userId") public String userId;
    @SerializedName("events") public List<SwipeEvent> events;
}

