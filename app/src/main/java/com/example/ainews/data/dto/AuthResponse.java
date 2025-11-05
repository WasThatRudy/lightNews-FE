package com.example.ainews.data.dto;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {
    @SerializedName("token") public String token;
    @SerializedName("accessToken") public String accessToken;
    @SerializedName("jwt") public String jwt;

    public String getToken() {
        if (token != null && !token.isEmpty()) return token;
        if (accessToken != null && !accessToken.isEmpty()) return accessToken;
        if (jwt != null && !jwt.isEmpty()) return jwt;
        return null;
    }
}


