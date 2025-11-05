package com.example.ainews.data;

import com.example.ainews.model.Article;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository placeholder to be wired to backend later (Retrofit/GraphQL/etc.).
 * Provides sample data for UI development.
 */
public class NewsRepository {
    private static NewsRepository instance;
    private int articleSeed = 1000;

    public static synchronized NewsRepository getInstance() {
        if (instance == null) instance = new NewsRepository();
        return instance;
    }

    public List<Article> getPersonalizedFeed() {
        return demoArticles("Smart feed");
    }

    public List<Article> searchArticles(String query) {
        return demoArticles("Results for: " + query);
    }

    public List<Article> getTrendingHighlights() {
        return demoArticles("Trending");
    }

    public List<Article> getMoreArticles(int count) {
        List<Article> list = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            articleSeed++;
            list.add(generateArticle("More"));
        }
        return list;
    }

    private List<Article> demoArticles(String prefix) {
        List<Article> list = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            list.add(generateArticle(prefix));
        }
        return list;
    }

    private Article generateArticle(String prefix) {
        articleSeed++;
        String idx = String.valueOf(articleSeed);
        return new Article(
                prefix + " Article " + idx,
                "Short summary of the article " + idx + ". AI-curated to match your interests.",
                "AI News",
                (articleSeed % 12) + "h",
                "https://picsum.photos/seed/ai" + idx + "/600/400",
                "https://example.com/article/" + idx
        );
    }
}


