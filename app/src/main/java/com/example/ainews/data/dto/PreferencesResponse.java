package com.example.ainews.data.dto;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PreferencesResponse {
    @SerializedName("userId") public String userId;
    @SerializedName("filters") public List<String> filters;
    @SerializedName("likes") public Integer likes;
    @SerializedName("dislikes") public Integer dislikes;
}


