package com.saylonn.chatapp;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.saylonn.chatapp.databinding.ActivityMainBinding;
import com.saylonn.chatapp.ui.dialogs.ErrorDialog;
import com.saylonn.chatapp.ui.dialogs.LoginDialog;

public class MainActivity extends AppCompatActivity {
    private boolean loggedIn = false;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);

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
    private void showNotificationNotAllowedDialog(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ErrorDialog errorDialog = new ErrorDialog();
        errorDialog.show(ft, "Notification not allowed");
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
               if (isGranted){
                   //FCM SDK (and your App) can post Notifications
               } else {
                   showNotificationNotAllowedDialog();
               }
            });
    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

}