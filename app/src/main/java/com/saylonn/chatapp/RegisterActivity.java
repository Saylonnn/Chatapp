package com.saylonn.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saylonn.chatapp.comm.VolleyCallbackListener;
import com.saylonn.chatapp.comm.VolleyRequest;

import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity implements VolleyCallbackListener {
    private final String TAG = "CAPP";
    private EditText username_tv;
    private EditText email_tv;
    private EditText password_1_tv;
    private EditText password_2_tv;
    private Button register_button;
    private ProgressBar progressBar;

    VolleyRequest volleyRequest;
    SharedPreferences sharedPreferences;
    String fcm_token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        volleyRequest = new VolleyRequest();
        volleyRequest.addCallbackListener(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        username_tv = findViewById(R.id.register_username);
        email_tv  = findViewById(R.id.register_email);
        password_1_tv = findViewById(R.id.register_password1);
        password_2_tv = findViewById(R.id.register_password2);
        register_button = findViewById(R.id.register_button);
        progressBar = findViewById(R.id.progressBar_registration);
        progressBar.setVisibility(View.GONE);

        fcm_token = sharedPreferences.getString(String.valueOf(R.string.token_key), "none");



        register_button.setOnClickListener(v -> {
            String username = username_tv.getText().toString();
            String email = email_tv.getText().toString();
            String password_1 = password_1_tv.getText().toString();
            String password_2 = password_2_tv.getText().toString();
            
            String text = "username: "+ username + " email: " + email + " password_1: "+ password_1 + " password_2: "+ password_2;
            Log.d(TAG, text);
            if(!fcm_token.equals("none")) {
                if (!email.equals("") && !username.equals("") && !password_1.equals("") && !password_2.equals("")) {

                    if (password_1.equals(password_2)) {
                        if(password_1.length() >= 8 && password_1.length() <= 20) {
                            volleyRequest.register(username, email, password_1, fcm_token, RegisterActivity.this);
                            progressBar.setVisibility(View.VISIBLE);
                            register_button.setEnabled(false);
                        }
                        else{
                            password_1_tv.setError(this.getString(R.string.correct_password_length));
                        }
                    }
                    else{
                        password_2_tv.setText("");
                        String errorText = this.getString(R.string.passwords_must_be_equals);
                        password_2_tv.setError(errorText);
                    }
                }else{
                    if(username.equals("")) {
                        String errorText = this.getString(R.string.field_not_empty_allowed);
                        username_tv.setError(errorText);
                    }
                    if(email.equals("")) {
                        email_tv.setError(this.getString(R.string.field_not_empty_allowed));
                    }
                    if(password_1.equals("")) {
                        password_1_tv.setError(this.getString(R.string.field_not_empty_allowed));
                    }
                    if(password_2.equals("")) {
                        password_2_tv.setError(this.getString(R.string.field_not_empty_allowed));
                    }
                }
            }
        });
    }

    @Override
    public void callbackMethod(String function, String message) {
        if(function.equals("register") && message.equals("accepted")){
            String text = " RegisterActivity" + "registration accepted";
            Log.d(TAG, text);
            finish();
        }else{
            progressBar.setVisibility(View.GONE);
            register_button.setEnabled(true);
        }
    }

    @Override
    public void jsonCallbackMethod(String function, JSONObject json){
        if(function.equals("register")) {
            Log.d(TAG, "RegisterActivity: jsonCallbackMethod called");
        }
    }
}