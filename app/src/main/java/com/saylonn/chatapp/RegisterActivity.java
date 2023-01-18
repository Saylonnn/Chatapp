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

import com.saylonn.chatapp.interfaces.VolleyCallbackListener;
import com.saylonn.chatapp.comm.VolleyRequest;

import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity implements VolleyCallbackListener {
    private final String TAG = "CAPP";
    private EditText usernameTv;
    private EditText emailTv;
    private EditText passwordTv1;
    private EditText passwordTv2;
    private Button registerButton;
    private ProgressBar progressBar;
    private VolleyRequest volleyRequest;
    private SharedPreferences sharedPreferences;
    private String fcmToken;

    /*
    * wird bei starten der Activity ausgeführt
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        volleyRequest = new VolleyRequest();
        volleyRequest.addCallbackListener(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        usernameTv = findViewById(R.id.register_username);
        emailTv  = findViewById(R.id.register_email);
        passwordTv1 = findViewById(R.id.register_password1);
        passwordTv2 = findViewById(R.id.register_password2);
        registerButton = findViewById(R.id.register_button);
        progressBar = findViewById(R.id.progressBar_registration);
        progressBar.setVisibility(View.GONE);

        fcmToken = sharedPreferences.getString(String.valueOf(R.string.token_key), "none");

        registerButton.setOnClickListener(v -> {
            String username = usernameTv.getText().toString();
            String email = emailTv.getText().toString();
            String password_1 = passwordTv1.getText().toString();
            String password_2 = passwordTv2.getText().toString();
            
            String text = "username: "+ username + " email: " + email + " password_1: "+ password_1 + " password_2: "+ password_2;
            Log.d(TAG, text);
            if(!fcmToken.equals("none")) {
                if (!email.equals("") && !username.equals("") && !password_1.equals("") && !password_2.equals("")) {

                    if (password_1.equals(password_2)) {
                        if(password_1.length() >= 8 && password_1.length() <= 20) {
                            volleyRequest.register(username, email, password_1, fcmToken, RegisterActivity.this);
                            progressBar.setVisibility(View.VISIBLE);
                            registerButton.setEnabled(false);
                        }
                        else{
                            passwordTv1.setError(this.getString(R.string.correct_password_length));
                        }
                    }
                    else{
                        passwordTv2.setText("");
                        String errorText = this.getString(R.string.passwords_must_be_equals);
                        passwordTv2.setError(errorText);
                    }
                }else{
                    if(username.equals("")) {
                        String errorText = this.getString(R.string.field_not_empty_allowed);
                        usernameTv.setError(errorText);
                    }
                    if(email.equals("")) {
                        emailTv.setError(this.getString(R.string.field_not_empty_allowed));
                    }
                    if(password_1.equals("")) {
                        passwordTv1.setError(this.getString(R.string.field_not_empty_allowed));
                    }
                    if(password_2.equals("")) {
                        passwordTv2.setError(this.getString(R.string.field_not_empty_allowed));
                    }
                }
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
        if(function.equals("register") && message.equals("accepted")){
            String text = " RegisterActivity" + "registration accepted";
            Log.d(TAG, text);
            finish();
        }else{
            progressBar.setVisibility(View.GONE);
            registerButton.setEnabled(true);
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
        if(function.equals("register")) {
            Log.d(TAG, "RegisterActivity: jsonCallbackMethod called");
        }
    }
}