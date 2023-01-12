package com.saylonn.chatapp.ui.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.saylonn.chatapp.R;
import com.saylonn.chatapp.comm.MyFirebaseMessagingService;
import com.saylonn.chatapp.comm.VolleyCallbackListener;
import com.saylonn.chatapp.comm.VolleyRequest;

public class LoginDialog extends Dialog implements View.OnClickListener, VolleyCallbackListener {
    private final String TAG = "VolleyRequest";
    private boolean isLoggedIn = false;
    private ProgressBar progressBar;
    public Activity c;
    private VolleyRequest volleyRequest = new VolleyRequest();
    private EditText loginField;
    private EditText passwordField;
    private Button loginButton;
    private SharedPreferences sharedPreferences;


    public LoginDialog(Activity a) {
        super(a);
        this.c = a;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_dialog);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        loginField = findViewById(R.id.loginDialogEmail);
        passwordField = findViewById(R.id.loginDialogPassword);
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);
        volleyRequest.addCallbackListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onclick performed");
        if(!loginField.getText().toString().equals("") && !passwordField.getText().toString().equals("")){
            String token = sharedPreferences.getString(String.valueOf(R.string.token_key), "none");
            if(!token.equals("none")) {
                volleyRequest.login(loginField.getText().toString(), passwordField.getText().toString(), "test_token", getContext());
                progressBar.setVisibility(View.VISIBLE);
            }else{
                Toast.makeText(this.getContext(), "Connect to internet", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this.getContext(), String.valueOf(R.string.empty_field_message), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void callbackMethod(String function, String parameter){
        if (function.equals("login")){
            if (parameter.equals("accepted")){

                this.isLoggedIn = true;
                this.dismiss();
            }
        }
        else{
            Log.d(TAG, "login not accepted");
            if(parameter.equals("declined")) {
                Toast.makeText(this.getContext(), String.valueOf(R.string.notAccepted), Toast.LENGTH_SHORT).show();
            }
            if(parameter.equals("serverError")) {
                Toast.makeText(this.getContext(), String.valueOf(R.string.notAccepted), Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.GONE);
            loginField.setText("");
            passwordField.setText("");

        }
    }
}
