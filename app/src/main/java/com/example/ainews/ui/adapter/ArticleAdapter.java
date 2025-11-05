package com.example.ainews.ui.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ainews.R;
import com.example.ainews.model.Article;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {
    private final List<Article> articles;

    public ArticleAdapter(List<Article> articles) {
        this.articles = articles;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = articles.get(position);
        holder.title.setText(article.getTitle());
        holder.summary.setText(article.getSummary());
        Glide.with(holder.thumb.getContext())
                .load(article.getImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.thumb);

        holder.itemView.setOnClickListener(v -> {
            if (article.getUrl() != null) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(article.getUrl()));
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articles != null ? articles.size() : 0;
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder {
        ImageView thumb;
        TextView title;
        TextView summary;
        ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.ivThumb);
            title = itemView.findViewById(R.id.tvTitle);
            summary = itemView.findViewById(R.id.tvSummary);
        }
    }
}


