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
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.google.firebase.messaging.FirebaseMessaging;
import com.saylonn.chatapp.databinding.ActivityMainBinding;
import com.saylonn.chatapp.ui.dialogs.ErrorDialog;
import com.saylonn.chatapp.ui.settings.SettingsFragment;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "CAPP";
    private boolean loggedIn = false;
    private ActivityMainBinding binding;
    private SharedPreferences encrpytedSharedPreferences;
    private String masterKeyAlias = null;
    //prüft ob NotificationPermisson gegeben wurde (ab Android 13)
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted){
                    //FCM SDK (and your App) can post Notifications
                } else {
                    showNotificationNotAllowedDialog();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        askNotificationPermission();

        /*
         * generieren des Verschüsselten SharedPreferences Objektes um das Verschlüsselte Passwort auszulesen
         */
        try {
            masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            encrpytedSharedPreferences = EncryptedSharedPreferences.create(
                    "secret_shared_prefs",
                    masterKeyAlias,
                    this,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

        /*
         * auslesen des aktuellen Firebasemessaging Tokens
         */
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

     //wird gestartet nach dem der firebase messaging Token erfolgreich ausgelesen wurde
    private void doCreate() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (!loggedIn) {
            String email = sp.getString(String.valueOf(R.string.login_email), "empty");
            String password = encrpytedSharedPreferences.getString(String.valueOf(R.string.login_password), "empty");
            String fcm_token = sp.getString(String.valueOf(R.string.token_key), "empty");
            Log.d(TAG, "Saved Credentials: email: " + email + " password: " + password);
            if (email.equals("empty") || password.equals("empty") || fcm_token.equals("empty")) {
                switchActivities();
            }
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_chats, R.id.navigation_search, R.id.navigation_profile, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }


    /**
     * zeigt einen Dialog wenn Notifications Erlaubnis nicht erteilt wurde
     */
    private void showNotificationNotAllowedDialog(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ErrorDialog errorDialog = new ErrorDialog();
        errorDialog.show(ft, "Notification not allowed");
    }

    /**
     * Fragt nach der Permission wenn sie nicht erteilt wurde (>= Anroid 13
     */
    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    /**
     * Führt den Switch zur LoginActivity durch
     */
    private void switchActivities(){
        Intent switchActivityIntent = new Intent(this, LoginActivity.class);
        startActivity(switchActivityIntent);
    }
}