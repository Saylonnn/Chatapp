package com.saylonn.chatapp.ui.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.saylonn.chatapp.LoginActivity;
import com.saylonn.chatapp.MainActivity;
import com.saylonn.chatapp.R;
import com.saylonn.chatapp.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {
    private TextView email;
    private TextView username;
    private Button logoutButton;
    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel searchViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        email = binding.displayEmailTV;
        username = binding.displayUsernameTV;
        logoutButton = binding.logoutButton;

        email.setText(sharedPreferences.getString(String.valueOf(R.string.login_email), "empty"));
        //final TextView textView = binding.textProfile;
        //searchViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().putString(String.valueOf(R.string.login_email), "empty").apply();
                sharedPreferences.edit().putString(String.valueOf(R.string.login_password), "empty").apply();

                switchToLogin();

            }
        });
        return root;
    }

    private void switchToLogin(){
        Intent switchActivityIntent = new Intent(getActivity(), LoginActivity.class);
        startActivity(switchActivityIntent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}