package com.example.ainews;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.ainews.ui.HomeFragment;
import com.example.ainews.ui.ProfileFragment;
import com.example.ainews.ui.SearchFragment;
import com.example.ainews.ui.TrendingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                replaceFragment(new HomeFragment());
                return true;
            } else if (id == R.id.nav_search) {
                replaceFragment(new SearchFragment());
                return true;
            } else if (id == R.id.nav_trending) {
                replaceFragment(new TrendingFragment());
                return true;
            } else if (id == R.id.nav_profile) {
                replaceFragment(new ProfileFragment());
                return true;
            }
            return false;
        });

        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }
    }

    private void replaceFragment(@NonNull Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}


