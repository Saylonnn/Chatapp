package com.saylonn.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saylonn.chatapp.interfaces.VolleyCallbackListener;
import com.saylonn.chatapp.comm.VolleyRequest;

import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class LoginActivity extends AppCompatActivity implements VolleyCallbackListener {
    private Button loginButton;
    private Button registerButton;
    private TextView emailTv;
    private TextView passwordTv;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;
    private VolleyRequest volleyRequest;
    private final String TAG = "CAPP";
    private SharedPreferences encryptedSharedPreferencs;
    private String masterKeyAlias = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Generiert das verschlüsselte SharedPreferences Objekt
        try {
            masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        try {
            encryptedSharedPreferencs = EncryptedSharedPreferences.create(
                    "secret_shared_prefs",
                    masterKeyAlias,
                    this,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        volleyRequest = new VolleyRequest();
        volleyRequest.addCallbackListener(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        emailTv = findViewById(R.id.loginEmail);
        passwordTv = findViewById(R.id.loginPassword);
        progressBar = findViewById(R.id.progressBar);

        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerBtn);
        String token = sharedPreferences.getString(String.valueOf(R.string.token_key), "none");
        Log.d(TAG + " LoginActivity", "token = " + token);

        loginButton.setOnClickListener(v -> {
            String email = emailTv.getText().toString();
            String password = passwordTv.getText().toString();
            if(!email.equals("") && !password.equals("")){
                if(!token.equals("none")) {
                    volleyRequest.login(email, password, token, LoginActivity.this);
                    progressBar.setVisibility(View.VISIBLE);
                    loginButton.setEnabled(false);
                }else{
                    String text = "Connect to internet " + token;
                    Toast.makeText(LoginActivity.this, text, Toast.LENGTH_SHORT).show();
                }

            }else{
                Toast.makeText(LoginActivity.this, "Bitte fülle alle Felder aus.", Toast.LENGTH_SHORT).show();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToRegisterActivity();
            }
        });
    }

    /**
     * Callbackmethode für die Asynchrone StringWebRequest
     * @param function übergibt den Namen der funktion die in der volleyRequst Klasse aufgerufen wurde
     *                 daran kann die Callbackmethode entscheiden ob sie etwas tun muss oder nicht
     * @param message ergebnis der Webrequest
     */
    @Override
    public void callbackMethod(String function, String message) {
        if(function.equals("login") && message.equals("accepted")){
            String text = " LoginActivity" + "login accepted";
            Log.d(TAG, text);
            emailTv = findViewById(R.id.loginEmail);
            passwordTv = findViewById(R.id.loginPassword);
            String email = emailTv.getText().toString();
            String password = passwordTv.getText().toString();
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            encryptedSharedPreferencs.edit().putString(String.valueOf(R.string.login_password), password).apply();
            sharedPreferences.edit().putString(String.valueOf(R.string.login_email), email).apply();
            finish();
        }
        else{
            progressBar.setVisibility(View.GONE);
            loginButton.setEnabled(true);
        }
    }

    /**
     * Callbackmethode für die Asynchrone JsonWebRequest
     * @param function übergibt den Namen der funktion die in der volleyRequst Klasse aufgerufen wurde
     *                 daran kann die Callbackmethode entscheiden ob sie etwas tun muss oder nicht
     * @param json ergebnis der JsonWebrequest
     */
    @Override
    public void jsonCallbackMethod(String function, JSONObject json){
        if(function.equals("login")) {
            Log.d(TAG, "LoginActivity: jsonCallbackMethod called");
        }
    }


    //führt den wechsel zur RegisterActivity durch
    private void switchToRegisterActivity(){
        Intent switchActivityIntent = new Intent(this, RegisterActivity.class);
        startActivity(switchActivityIntent);
    }

    //Verhindert das man die LoginActivity einfach zum MainScreen verlassen kann. Stattdessen schließt sich die App
    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
    }
}