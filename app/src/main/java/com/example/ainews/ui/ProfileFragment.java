package com.example.ainews.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ainews.R;

public class ProfileFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Button save = view.findViewById(R.id.btnSavePrefs);
        CheckBox cbTech = view.findViewById(R.id.cbTech);
        CheckBox cbBusiness = view.findViewById(R.id.cbBusiness);
        CheckBox cbSports = view.findViewById(R.id.cbSports);

        save.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Preferences saved", Toast.LENGTH_SHORT).show();
        });
        return view;
    }
}


