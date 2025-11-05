package com.example.ainews.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ainews.R;
import com.example.ainews.data.NewsRepository;
import com.example.ainews.model.Article;
import com.example.ainews.ui.adapter.ArticleAdapter;

import java.util.List;

public class TrendingFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trending, container, false);
        RecyclerView rv = view.findViewById(R.id.rvTrending);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        List<Article> trending = NewsRepository.getInstance().getTrendingHighlights();
        rv.setAdapter(new ArticleAdapter(trending));
        return view;
    }
}


