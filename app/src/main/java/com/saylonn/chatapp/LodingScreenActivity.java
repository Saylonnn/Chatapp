package com.saylonn.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class LodingScreenActivity extends AppCompatActivity {
    private final String TAG = "CAPP";
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loding_screen);
        Log.d(TAG, "LoadingScreenActivity loaded");
        progressBar = findViewById(R.id.progressBarLoadingScreen);
        progressBar.setVisibility(View.VISIBLE);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.d(TAG, "Fetching FCM Registration token failed " + task.getException());
                            closeLoadingScreen();
                        }
                        String token = task.getResult();
                        String msg = "new genereted Token: "+  token;
                        Log.d(TAG, msg);
                        sp.edit().putString(String.valueOf(R.string.token_key), token).apply();
                        closeLoadingScreen();
                    }
                });
    }

    private void closeLoadingScreen(){
        progressBar.setVisibility(View.GONE);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String token = sp.getString(String.valueOf(R.string.token_key), "none");

        if (token.equals("none")){
            Toast.makeText(LodingScreenActivity.this, String.valueOf(R.string.connectToInternet), Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Log.d(TAG, "Token: " + token);
            finish();
        }
    }
}