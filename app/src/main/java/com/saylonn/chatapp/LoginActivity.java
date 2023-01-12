package com.saylonn.chatapp;

import androidx.appcompat.app.AppCompatActivity;

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
    Button loginButton;
    TextView email_tv;
    TextView password_tv;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    VolleyRequest volleyRequest;
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
        String token = sharedPreferences.getString(String.valueOf(R.string.token_key), "none");
        Log.d(TAG + " LoginActivity", "token = " + token);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_tv.getText().toString();
                String password = password_tv.getText().toString();
                if(!email.equals("") && !password.equals("")){
                    if(!token.equals("none")) {
                        volleyRequest.login(email, password, token, LoginActivity.this);
                        progressBar.setVisibility(View.VISIBLE);
                    }else{
                        Toast.makeText(LoginActivity.this, "Connect to internet" + token, Toast.LENGTH_SHORT).show();

                    }

                }else{
                    Toast.makeText(LoginActivity.this, String.valueOf(R.string.empty_field_message), Toast.LENGTH_SHORT).show();
                }
            }
    });
    }


    @Override
    public void callbackMethod(String function, String message) {
        if(function.equals("login") && message.equals("accepted")){
            Log.d(TAG + " LoginActivity", "login accepted");
            finish();
        }
        else{
            progressBar.setVisibility(View.GONE);
        }
    }
}