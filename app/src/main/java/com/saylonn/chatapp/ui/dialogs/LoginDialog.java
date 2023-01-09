package com.saylonn.chatapp.ui.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.saylonn.chatapp.R;
import com.saylonn.chatapp.comm.VolleyRequest;

public class LoginDialog extends Dialog implements View.OnClickListener{

    public Activity c;
    VolleyRequest volleyRequest = new VolleyRequest();
    EditText loginField;
    EditText passwordField;
    Button loginButton;

    public LoginDialog(Activity a) {
        super(a);
        this.c = a;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_dialog);
        loginField = findViewById(R.id.loginDialogEmail);
        passwordField = findViewById(R.id.loginDialogPassword);
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(!loginField.getText().toString().equals("") && !passwordField.getText().toString().equals("")){
            volleyRequest.login(loginField.getText().toString(), passwordField.getText().toString(), "test_token", getContext());
        }
    }
}
