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
import com.saylonn.chatapp.comm.VolleyRequest;

public class LoginDialog extends Dialog implements View.OnClickListener{
    private final String TAG = "VolleyRequest";
    private ProgressBar progressBar;
    public Activity c;
    VolleyRequest volleyRequest = new VolleyRequest();
    EditText loginField;
    EditText passwordField;
    Button loginButton;
    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());

    public LoginDialog(Activity a) {
        super(a);
        this.c = a;
    }

    protected void onCreate(Bundle savedInstanceState) {
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
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
            progressBar.setVisibility(View.VISIBLE);
            while(sp.getString(String.valueOf(R.string.login_status), "not_tried").equals("not_tried")){

                try{
                    Thread.sleep(100);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                Log.d(TAG, sp.getString(String.valueOf(R.string.login_status), "not_set"));
            }
            progressBar.setVisibility(View.GONE);
            dismiss();


            /*
            progressBar = (ProgressBar)findViewById(R.id.progressBar);

            progressBar.setVisibility(View.VISIBLE);
            while(sp.getString(String.valueOf(R.string.login_status), "not_tried").equals("not_tried")){
                try{
                    Thread.sleep(10);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            */
        }else{
            Toast.makeText(this.getContext(), String.valueOf(R.string.empty_field_message), Toast.LENGTH_SHORT).show();
        }
    }
}
