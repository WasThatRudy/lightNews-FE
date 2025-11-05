package com.example.ainews.auth;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.example.ainews.MainActivity;
import com.example.ainews.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.example.ainews.data.TokenStore;

public class AuthActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        TokenStore.init(getApplicationContext());

        ViewPager2 pager = findViewById(R.id.pager);
        TabLayout tabs = findViewById(R.id.tabLayout);
        pager.setAdapter(new AuthPagerAdapter(this));

        new TabLayoutMediator(tabs, pager, (tab, position) -> {
            tab.setText(position == 0 ? "Login" : "Signup");
        }).attach();
    }

    public void onAuthSuccess() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}


