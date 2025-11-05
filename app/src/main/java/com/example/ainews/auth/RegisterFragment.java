package com.example.ainews.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ainews.R;

public class RegisterFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_auth_simple, container, false);
        EditText etUser = v.findViewById(R.id.etUsername);
        EditText etPass = v.findViewById(R.id.etPassword);
        Button btn = v.findViewById(R.id.btnAction);
        btn.setText("Register");
        btn.setOnClickListener(view -> {
            if (TextUtils.isEmpty(etUser.getText()) || TextUtils.isEmpty(etPass.getText())) {
                Toast.makeText(requireContext(), "Enter username and password", Toast.LENGTH_SHORT).show();
                return;
            }
            String userId = etUser.getText().toString().trim();
            String password = etPass.getText().toString().trim();
            com.example.ainews.data.NewsRepository.getInstance().setUserId(requireContext(), userId);
            btn.setEnabled(false);
            btn.setText("Registering...");
            // For signup, call /api/init to create the user and get initial articles
            com.example.ainews.data.NewsRepository.getInstance().bootstrapUser(userId, password, true,
                new com.example.ainews.data.NewsRepository.BootstrapCallback() {
                    @Override
                    public void onSuccess(com.example.ainews.data.dto.InitResponse response) {
                        requireActivity().runOnUiThread(() -> {
                            ((AuthActivity) requireActivity()).onAuthSuccess();
                        });
                    }
                    @Override
                    public void onError() {
                        requireActivity().runOnUiThread(() -> {
                            btn.setEnabled(true);
                            btn.setText("Register");
                            Toast.makeText(requireContext(), "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            );
        });
        return v;
    }
}


