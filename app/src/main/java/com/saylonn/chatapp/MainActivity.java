package com.saylonn.chatapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.saylonn.chatapp.databinding.ActivityMainBinding;
import com.saylonn.chatapp.ui.dialogs.LoginDialog;

public class MainActivity extends AppCompatActivity {
    private boolean loggedIn = false;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        String username = sp.getString("login_username", "empty");
        String password = sp.getString("login_pw", "empty");
        String fcm_token = sp.getString("fcm_token", "empty");

        if (username.equals("empty") || password.equals("empty") || fcm_token.equals("empty")){
            showLoginDialog();
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_chats, R.id.navigation_search, R.id.navigation_profile, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    private void showLoginDialog(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();


        LoginDialog loginDialog = new LoginDialog();
        loginDialog.show(ft, "login");
    }

}