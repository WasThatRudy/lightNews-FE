package com.example.ainews.data.dto;

import com.google.gson.annotations.SerializedName;

public class AuthRequest {
    @SerializedName("userId") public String userId;
    @SerializedName("username") public String username; // some backends expect "username"
    @SerializedName("password") public String password;
}


