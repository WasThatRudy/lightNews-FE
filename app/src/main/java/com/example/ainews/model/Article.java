package com.example.ainews.model;

public class Article {
    private final String title;
    private final String summary;
    private final String source;
    private final String timeAgo;
    private final String imageUrl;
    private final String url;

    public Article(String title, String summary, String source, String timeAgo, String imageUrl, String url) {
        this.title = title;
        this.summary = summary;
        this.source = source;
        this.timeAgo = timeAgo;
        this.imageUrl = imageUrl;
        this.url = url;
    }

    public String getTitle() { return title; }
    public String getSummary() { return summary; }
    public String getSource() { return source; }
    public String getTimeAgo() { return timeAgo; }
    public String getImageUrl() { return imageUrl; }
    public String getUrl() { return url; }
}


