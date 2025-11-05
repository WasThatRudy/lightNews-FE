package com.example.ainews.data;

import androidx.annotation.Nullable;

import com.example.ainews.data.api.NewsApiService;
import com.example.ainews.data.dto.ArticleDto;
import com.example.ainews.data.dto.FeedResponse;
import com.example.ainews.data.dto.InitRequest;
import com.example.ainews.data.dto.InitResponse;
import com.example.ainews.data.dto.SwipeEvent;
import com.example.ainews.data.dto.SwipeRequest;
import com.example.ainews.data.dto.SwipeResponse;
import com.example.ainews.model.Article;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsRepository {
    private static NewsRepository instance;
    private final NewsApiService api = ApiClient.getClient().create(NewsApiService.class);

    public interface ArticlesCallback { void onResult(List<Article> articles); }

    public static synchronized NewsRepository getInstance() {
        if (instance == null) instance = new NewsRepository();
        return instance;
    }

    public String ensureUserId(android.content.Context ctx) {
        android.content.SharedPreferences p = android.preference.PreferenceManager.getDefaultSharedPreferences(ctx);
        String id = p.getString("user_id", null);
        if (id == null) {
            id = "user-" + UUID.randomUUID();
            p.edit().putString("user_id", id).apply();
        }
        return id;
    }

    public void setUserId(android.content.Context ctx, String userId) {
        android.content.SharedPreferences p = android.preference.PreferenceManager.getDefaultSharedPreferences(ctx);
        p.edit().putString("user_id", userId).apply();
    }

    public String getUserId(android.content.Context ctx) {
        android.content.SharedPreferences p = android.preference.PreferenceManager.getDefaultSharedPreferences(ctx);
        return p.getString("user_id", null);
    }

    public interface BootstrapCallback {
        void onSuccess(InitResponse response);
        void onError();
    }

    public void bootstrapUser(String userId, String password, boolean isSignup, BootstrapCallback callback) {
        // First call auth endpoint to get JWT, then call /api/init
        com.example.ainews.data.dto.AuthRequest auth = new com.example.ainews.data.dto.AuthRequest();
        auth.userId = userId;
        auth.username = userId;
        auth.password = password;
        retrofit2.Call<com.example.ainews.data.dto.AuthResponse> call = isSignup ?
                api.register(auth) : api.login(auth);
        call.enqueue(new Callback<com.example.ainews.data.dto.AuthResponse>() {
            @Override public void onResponse(Call<com.example.ainews.data.dto.AuthResponse> call, Response<com.example.ainews.data.dto.AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String tok = response.body().getToken();
                    android.util.Log.d("NewsRepository", "Auth response: token=" + (tok != null ? "present" : "null"));
                    if (tok != null && !tok.isEmpty()) {
                        TokenStore.setToken(tok);
                        // now init feed
                        InitRequest req = new InitRequest();
                        req.userId = userId;
                        req.filters = new ArrayList<>();
                        req.diversify = true;
                        api.init(req).enqueue(new Callback<InitResponse>() {
                        @Override public void onResponse(Call<InitResponse> call2, Response<InitResponse> resp2) {
                            android.util.Log.d("NewsRepository", "Init response: " + resp2.code() + " success=" + resp2.isSuccessful());
                            if (resp2.isSuccessful()) {
                                InitResponse body = resp2.body();
                                if (body == null) {
                                    body = new InitResponse();
                                    body.articles = new ArrayList<>();
                                }
                                callback.onSuccess(body);
                            } else {
                                try {
                                    String errorBody = resp2.errorBody() != null ? resp2.errorBody().string() : "no error body";
                                    android.util.Log.e("NewsRepository", "Init failed: " + resp2.code() + " message=" + resp2.message() + " body=" + errorBody);
                                } catch (java.io.IOException e) {
                                    android.util.Log.e("NewsRepository", "Init failed: " + resp2.code() + " message=" + resp2.message());
                                }
                                // Even if init fails, allow user to proceed (they can use feed endpoint)
                                InitResponse emptyBody = new InitResponse();
                                emptyBody.articles = new ArrayList<>();
                                callback.onSuccess(emptyBody);
                            }
                        }
                            @Override public void onFailure(Call<InitResponse> call2, Throwable t2) {
                                android.util.Log.e("NewsRepository", "Init network error", t2);
                                callback.onError();
                            }
                        });
                        return;
                    }
                }
                // If signup failed (e.g., 400 - already exists), attempt login automatically
                if (isSignup && response.code() == 400) {
                    android.util.Log.d("NewsRepository", "Register returned 400, trying login...");
                    api.login(auth).enqueue(new Callback<com.example.ainews.data.dto.AuthResponse>() {
                        @Override public void onResponse(Call<com.example.ainews.data.dto.AuthResponse> c2, Response<com.example.ainews.data.dto.AuthResponse> r2) {
                            if (r2.isSuccessful() && r2.body() != null) {
                                String tok2 = r2.body().getToken();
                                android.util.Log.d("NewsRepository", "Login response: token=" + (tok2 != null ? "present" : "null"));
                                if (tok2 != null && !tok2.isEmpty()) {
                                    TokenStore.setToken(tok2);
                                    InitRequest req = new InitRequest();
                                    req.userId = userId;
                                    req.filters = new ArrayList<>();
                                    req.diversify = true;
                                    api.init(req).enqueue(new Callback<InitResponse>() {
                                        @Override public void onResponse(Call<InitResponse> call3, Response<InitResponse> r3) {
                                            android.util.Log.d("NewsRepository", "Init after login: " + r3.code() + " success=" + r3.isSuccessful());
                                            if (r3.isSuccessful()) {
                                                InitResponse body = r3.body();
                                                if (body == null) {
                                                    body = new InitResponse();
                                                    body.articles = new ArrayList<>();
                                                }
                                                callback.onSuccess(body);
                                            } else {
                                                try {
                                                    String errorBody = r3.errorBody() != null ? r3.errorBody().string() : "no error body";
                                                    android.util.Log.e("NewsRepository", "Init after login failed: " + r3.code() + " body=" + errorBody);
                                                } catch (java.io.IOException e) {
                                                    android.util.Log.e("NewsRepository", "Init after login failed: " + r3.code());
                                                }
                                                // Even if init fails, allow user to proceed
                                                InitResponse emptyBody = new InitResponse();
                                                emptyBody.articles = new ArrayList<>();
                                                callback.onSuccess(emptyBody);
                                            }
                                        }
                                        @Override public void onFailure(Call<InitResponse> call3, Throwable t3) {
                                            android.util.Log.e("NewsRepository", "Init after login network error", t3);
                                            callback.onError();
                                        }
                                    });
                                    return;
                                }
                            }
                            android.util.Log.e("NewsRepository", "Login fallback failed: " + r2.code());
                            callback.onError();
                        }
                        @Override public void onFailure(Call<com.example.ainews.data.dto.AuthResponse> c2, Throwable t2) {
                            android.util.Log.e("NewsRepository", "Login fallback network error", t2);
                            callback.onError();
                        }
                    });
                } else {
                    android.util.Log.e("NewsRepository", "Auth failed: code=" + response.code() + " body=" + (response.body() != null ? "present" : "null"));
                    callback.onError();
                }
            }
            @Override public void onFailure(Call<com.example.ainews.data.dto.AuthResponse> call, Throwable t) {
                android.util.Log.e("NewsRepository", "Auth network error", t);
                callback.onError();
            }
        });
    }

    public void initFeed(String userId, @Nullable List<String> filters, boolean diversify, ArticlesCallback cb) {
        InitRequest req = new InitRequest();
        req.userId = userId;
        req.filters = filters;
        req.diversify = diversify;
        api.init(req).enqueue(new Callback<InitResponse>() {
            @Override public void onResponse(Call<InitResponse> call, Response<InitResponse> response) {
                if (!response.isSuccessful()) {
                    android.util.Log.e("NewsRepository", "Init failed: " + response.code() + " " + response.message());
                    cb.onResult(new ArrayList<>());
                    return;
                }
                List<Article> mapped = map(response.body() != null ? response.body().articles : null);
                cb.onResult(mapped);
            }
            @Override public void onFailure(Call<InitResponse> call, Throwable t) {
                android.util.Log.e("NewsRepository", "Init network error", t);
                cb.onResult(new ArrayList<>());
            }
        });
    }

    public void swipe(String userId, List<SwipeEvent> events, ArticlesCallback cb) {
        SwipeRequest req = new SwipeRequest();
        req.userId = userId;
        req.events = events;
        api.swipe(req).enqueue(new Callback<SwipeResponse>() {
            @Override public void onResponse(Call<SwipeResponse> call, Response<SwipeResponse> response) {
                if (!response.isSuccessful()) {
                    android.util.Log.e("NewsRepository", "Swipe failed: " + response.code() + " " + response.message());
                    cb.onResult(new ArrayList<>());
                    return;
                }
                cb.onResult(map(response.body() != null ? response.body().articles : null));
            }
            @Override public void onFailure(Call<SwipeResponse> call, Throwable t) {
                android.util.Log.e("NewsRepository", "Swipe network error", t);
                cb.onResult(new ArrayList<>());
            }
        });
    }

    public void feed(String userId, String category, int pageSize, ArticlesCallback cb) {
        api.feed(userId, category, pageSize).enqueue(new Callback<FeedResponse>() {
            @Override public void onResponse(Call<FeedResponse> call, Response<FeedResponse> response) {
                if (!response.isSuccessful()) {
                    android.util.Log.e("NewsRepository", "Feed failed: " + response.code() + " " + response.message());
                    cb.onResult(new ArrayList<>());
                    return;
                }
                cb.onResult(map(response.body() != null ? response.body().articles : null));
            }
            @Override public void onFailure(Call<FeedResponse> call, Throwable t) {
                android.util.Log.e("NewsRepository", "Feed network error", t);
                cb.onResult(new ArrayList<>());
            }
        });
    }

    private List<Article> map(List<ArticleDto> dtos) {
        List<Article> list = new ArrayList<>();
        if (dtos == null) return list;
        for (ArticleDto d : dtos) {
            String title = d.title != null ? d.title : "";
            String summary = d.description != null ? d.description : "";
            String source = d.source != null ? d.source.name : "";
            String time = d.publishedAt != null ? d.publishedAt : "";
            String img = d.urlToImage;
            String url = d.url;
            String category = d.category;
            list.add(new Article(title, summary, source, time, img, url, category));
        }
        return list;
    }
}


