package com.example.ainews.data.dto;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class UpdateFiltersRequest {
    @SerializedName("filters") public List<String> filters;
}


