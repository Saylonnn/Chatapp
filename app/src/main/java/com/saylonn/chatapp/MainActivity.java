package com.saylonn.chatapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.messaging.FirebaseMessaging;
import com.saylonn.chatapp.databinding.ActivityMainBinding;
import com.saylonn.chatapp.ui.dialogs.ErrorDialog;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "CAPP";
    private boolean loggedIn = false;
    private ActivityMainBinding binding;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.d(TAG, "Fetching FCM Registration token failed " + task.getException());

                    }
                    String token = task.getResult();
                    String msg = "new genereted Token: " + token;
                    Log.d(TAG, msg);
                    sp.edit().putString(String.valueOf(R.string.token_key), token).apply();
                    doCreate();
                });
    }

    private void doCreate() {
        context = getApplicationContext();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (!loggedIn) {
            String email = sp.getString(String.valueOf(R.string.login_email), "empty");
            String password = sp.getString(String.valueOf(R.string.login_password), "empty");
            String fcm_token = sp.getString(String.valueOf(R.string.token_key), "empty");
            Log.d(TAG, "Saved Credentials: email: " + email + " password: " + password);
            if (email.equals("empty") || password.equals("empty") || fcm_token.equals("empty")) {
                switchActivities();
            }
        }

        loggedIn = true;
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

    private void switchActivities(){
        Intent switchActivityIntent = new Intent(this, LoginActivity.class);
        startActivity(switchActivityIntent);
    }

    public void setLoggedIn(){
        loggedIn = true;
    }

}