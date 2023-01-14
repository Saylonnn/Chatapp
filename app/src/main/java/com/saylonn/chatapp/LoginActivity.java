package com.saylonn.chatapp;

import androidx.appcompat.app.AppCompatActivity;

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

import com.saylonn.chatapp.comm.VolleyCallbackListener;
import com.saylonn.chatapp.comm.VolleyRequest;
import com.saylonn.chatapp.interfaces.CallbackInterface;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity implements VolleyCallbackListener {
    private Button loginButton;
    private Button registerButton;
    private TextView email_tv;
    private TextView password_tv;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;
    private VolleyRequest volleyRequest;
    private final String TAG = "CAPP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        volleyRequest = new VolleyRequest();
        volleyRequest.addCallbackListener(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


        email_tv = findViewById(R.id.loginEmail);
        password_tv = findViewById(R.id.loginPassword);
        progressBar = findViewById(R.id.progressBar);

        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerBtn);
        String token = sharedPreferences.getString(String.valueOf(R.string.token_key), "none");
        Log.d(TAG + " LoginActivity", "token = " + token);

        loginButton.setOnClickListener(v -> {
            String email = email_tv.getText().toString();
            String password = password_tv.getText().toString();
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


    @Override
    public void callbackMethod(String function, String message) {
        if(function.equals("login") && message.equals("accepted")){
            String text = " LoginActivity" + "login accepted";
            Log.d(TAG, text);
            email_tv = findViewById(R.id.loginEmail);
            password_tv = findViewById(R.id.loginPassword);
            String email = email_tv.getText().toString();
            String password = password_tv.getText().toString();
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            sharedPreferences.edit().putString(String.valueOf(R.string.login_password), password).apply();
            sharedPreferences.edit().putString(String.valueOf(R.string.login_email), email).apply();
            finish();
        }
        else{
            progressBar.setVisibility(View.GONE);
            loginButton.setEnabled(true);
        }
    }

    private void switchToRegisterActivity(){
        Intent switchActivityIntent = new Intent(this, RegisterActivity.class);
        startActivity(switchActivityIntent);
    }
}