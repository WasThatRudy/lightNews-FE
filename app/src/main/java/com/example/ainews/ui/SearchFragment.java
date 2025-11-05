package com.example.ainews.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

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

public class SearchFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        EditText etQuery = view.findViewById(R.id.etQuery);
        RecyclerView rv = view.findViewById(R.id.rvSearchResults);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        etQuery.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String q = v.getText().toString().trim();
                if (!TextUtils.isEmpty(q)) {
                    List<Article> results = NewsRepository.getInstance().searchArticles(q);
                    rv.setAdapter(new ArticleAdapter(results));
                }
                return true;
            }
            return false;
        });

        return view;
    }
}


