package com.example.ainews.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ainews.R;
import com.example.ainews.data.NewsRepository;
import com.example.ainews.model.Article;
import com.example.ainews.ui.adapter.ArticleAdapter;
import android.speech.tts.TextToSpeech;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Switch;
import java.util.Locale;

import java.util.List;

public class HomeFragment extends Fragment {
    private TextToSpeech tts;
    private boolean ttsReady = false;
    private int swipeCount = 0;
    private final List<String> feedbackBuffer = new java.util.ArrayList<>();
    private SharedPreferences prefs;
    private Switch switchTTS;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rvCards);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        View tvEmpty = view.findViewById(R.id.tvEmpty);
        switchTTS = view.findViewById(R.id.switchTTS);
        
        prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        boolean ttsEnabled = prefs.getBoolean("tts_enabled", true);
        switchTTS.setChecked(ttsEnabled);
        
        switchTTS.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("tts_enabled", isChecked).apply();
        });

        List<Article> articles = new java.util.ArrayList<>(NewsRepository.getInstance().getPersonalizedFeed().subList(0, 10));
        ArticleAdapter adapter = new ArticleAdapter(articles);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();
                if (position < 0 || position >= articles.size()) return;
                Article swiped = articles.get(position);
                String feedback = direction == ItemTouchHelper.RIGHT ? "like" : "dislike";
                feedbackBuffer.add(feedback);
                swipeCount++;
                articles.remove(position);
                adapter.notifyItemRemoved(position);

                if (ttsReady && prefs.getBoolean("tts_enabled", true)) {
                    tts.speak(feedback.equals("like") ? "Liked" : "Disliked", TextToSpeech.QUEUE_FLUSH, null, "swipe_feedback");
                }

                if (swipeCount % 5 == 0) {
                    // Fake backend: send feedback and fetch 5 new items
                    List<Article> more = NewsRepository.getInstance().getMoreArticles(5);
                    int start = articles.size();
                    articles.addAll(more);
                    adapter.notifyItemRangeInserted(start, more.size());
                    feedbackBuffer.clear();
                }

                if (articles.isEmpty()) {
                    tvEmpty.setVisibility(View.VISIBLE);
                } else {
                    tvEmpty.setVisibility(View.GONE);
                }
            }
        };

        new ItemTouchHelper(callback).attachToRecyclerView(recyclerView);

        // TTS setup and onboarding prompt on first launch
        boolean firstLaunchDone = prefs.getBoolean("first_launch_done", false);
        tts = new TextToSpeech(requireContext(), status -> {
            ttsReady = status == TextToSpeech.SUCCESS && tts.setLanguage(Locale.getDefault()) >= 0;
            if (ttsReady && !firstLaunchDone && prefs.getBoolean("tts_enabled", true)) {
                tts.speak("Swipe left if you don't like the news, swipe right if you do.", TextToSpeech.QUEUE_FLUSH, null, "onboarding");
                prefs.edit().putBoolean("first_launch_done", true).apply();
            }
        });

        tvEmpty.setVisibility(articles.isEmpty() ? View.VISIBLE : View.GONE);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}


