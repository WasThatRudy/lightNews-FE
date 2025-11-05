package com.example.ainews.data;

import android.content.Context;
import android.preference.PreferenceManager;

public class TokenStore {
    private static Context appContext;

    public static void init(Context ctx) { appContext = ctx.getApplicationContext(); }

    public static void setToken(String token) {
        if (appContext == null) return;
        PreferenceManager.getDefaultSharedPreferences(appContext)
                .edit().putString("auth_token", token).apply();
    }

    public static String getToken() {
        if (appContext == null) return null;
        return PreferenceManager.getDefaultSharedPreferences(appContext)
                .getString("auth_token", null);
    }
}


