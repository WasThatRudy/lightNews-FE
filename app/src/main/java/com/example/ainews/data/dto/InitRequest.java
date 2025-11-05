package com.example.ainews.data.dto;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class InitRequest {
    @SerializedName("userId") public String userId;
    @SerializedName("filters") public List<String> filters;
    @SerializedName("diversify") public boolean diversify;
}

