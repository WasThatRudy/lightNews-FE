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
import android.widget.ImageButton;
import android.widget.TextView;
import android.app.AlertDialog;
import java.util.Locale;

import java.util.List;

public class HomeFragment extends Fragment {
    private TextToSpeech tts;
    private boolean ttsReady = false;
    private int swipeCount = 0;
    private final List<com.example.ainews.data.dto.SwipeEvent> feedbackBuffer = new java.util.ArrayList<>();
    private SharedPreferences prefs;
    private Switch switchTTS;
    private String selectedCategory = null; // null means all

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rvCards);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        View tvEmpty = view.findViewById(R.id.tvEmpty);
        TextView tvSwipeFeedback = view.findViewById(R.id.tvSwipeFeedback);
        switchTTS = view.findViewById(R.id.switchTTS);
        ImageButton btnFilter = view.findViewById(R.id.btnFilter);
        
        prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        boolean ttsEnabled = prefs.getBoolean("tts_enabled", true);
        switchTTS.setChecked(ttsEnabled);
        
        switchTTS.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("tts_enabled", isChecked).apply();
        });

        String userId = NewsRepository.getInstance().ensureUserId(requireContext());
        List<Article> articles = new java.util.ArrayList<>();
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
                com.example.ainews.data.dto.SwipeEvent event = new com.example.ainews.data.dto.SwipeEvent();
                event.category = swiped.getCategory();
                event.articleUrl = swiped.getUrl();
                event.reaction = feedback;
                feedbackBuffer.add(event);
                swipeCount++;
                articles.remove(position);
                adapter.notifyItemRemoved(position);

                if (ttsReady && prefs.getBoolean("tts_enabled", true)) {
                    tts.speak(feedback.equals("like") ? "Liked" : "Disliked", TextToSpeech.QUEUE_FLUSH, null, "swipe_feedback");
                }

                // Show overlay feedback text
                tvSwipeFeedback.setText(feedback.equals("like") ? "Liked" : "Disliked");
                tvSwipeFeedback.setTextColor(feedback.equals("like") ? 0xFF0A7E07 : 0xFFB00020);
                tvSwipeFeedback.setVisibility(View.VISIBLE);
                tvSwipeFeedback.postDelayed(() -> tvSwipeFeedback.setVisibility(View.GONE), 700);

                if (swipeCount % 5 == 0) {
                    // Send feedback to backend and append 5 new items
                    java.util.List<com.example.ainews.data.dto.SwipeEvent> events = new java.util.ArrayList<>(feedbackBuffer);
                    NewsRepository.getInstance().swipe(userId, events, more -> {
                        int start = articles.size();
                        articles.addAll(more);
                        adapter.notifyItemRangeInserted(start, more.size());
                    });
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

        btnFilter.setOnClickListener(v -> {
            String[] categories = new String[]{"All","business","entertainment","general","health","science","sports","technology"};
            int checked = 0;
            if (selectedCategory != null) {
                for (int i = 1; i < categories.length; i++) if (categories[i].equals(selectedCategory)) checked = i; 
            }
            final int prechecked = checked;
            new AlertDialog.Builder(requireContext())
                    .setTitle("Category")
                    .setSingleChoiceItems(categories, prechecked, (dialog, which) -> {
                        // nothing yet
                    })
                    .setPositiveButton("Apply", (dialog, which) -> {
                        AlertDialog d = (AlertDialog) dialog;
                        int idx = d.getListView().getCheckedItemPosition();
                        selectedCategory = (idx <= 0) ? null : categories[idx];
                        // refresh list with 10 items for selection
                        if (selectedCategory == null) {
                            NewsRepository.getInstance().initFeed(userId, null, true, fresh -> {
                                articles.clear();
                                articles.addAll(fresh);
                                adapter.notifyDataSetChanged();
                                tvEmpty.setVisibility(articles.isEmpty() ? View.VISIBLE : View.GONE);
                            });
                        } else {
                            NewsRepository.getInstance().feed(userId, selectedCategory, 10, fresh -> {
                                articles.clear();
                                articles.addAll(fresh);
                                adapter.notifyDataSetChanged();
                                tvEmpty.setVisibility(articles.isEmpty() ? View.VISIBLE : View.GONE);
                            });
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        // TTS setup and onboarding prompt on first launch
        boolean firstLaunchDone = prefs.getBoolean("first_launch_done", false);
        tts = new TextToSpeech(requireContext(), status -> {
            ttsReady = status == TextToSpeech.SUCCESS && tts.setLanguage(Locale.getDefault()) >= 0;
            if (ttsReady && !firstLaunchDone && prefs.getBoolean("tts_enabled", true)) {
                tts.speak("Swipe left if you don't like the news, swipe right if you do.", TextToSpeech.QUEUE_FLUSH, null, "onboarding");
                prefs.edit().putBoolean("first_launch_done", true).apply();
            }
        });

        // Load initial 10 articles from backend
        NewsRepository.getInstance().initFeed(userId, null, true, initial -> {
            articles.clear();
            if (selectedCategory != null) {
                // if a category was chosen before load, fetch that feed instead
                NewsRepository.getInstance().feed(userId, selectedCategory, 10, filtered -> {
                    articles.clear();
                    articles.addAll(filtered);
                    adapter.notifyDataSetChanged();
                    tvEmpty.setVisibility(articles.isEmpty() ? View.VISIBLE : View.GONE);
                });
            } else {
                articles.addAll(initial);
                adapter.notifyDataSetChanged();
                tvEmpty.setVisibility(articles.isEmpty() ? View.VISIBLE : View.GONE);
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


