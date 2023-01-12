package com.saylonn.chatapp.comm;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.saylonn.chatapp.comm.MyFirebaseMessagingService;
import com.saylonn.chatapp.R;
import com.saylonn.chatapp.interfaces.CallbackInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VolleyRequest {
    private static final String TAG = "VolleyRequest";
    String url = "https://www.api.caylonn.de:1337";


    public void login(String email, String password, String token, Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(String.valueOf(R.string.login_status), "not_tried");
        Log.d(TAG, "login called with "+ email + " " + password + " " + token);
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //String token = sharedPreferences.getString("token_key", "none");
        ///Log.d(TAG, "token: "+ token);
        Map<String, String> headerParams = new HashMap<String, String>();
        headerParams.put("email", email);
        headerParams.put("password", password);
        headerParams.put("token", token);
        //headerParams.put("token", token);
        doLoginRequest("login", "/auth/login", headerParams, Request.Method.GET, context);

    }

    public void doLoginRequest(String function, String urlExtension, Map<String, String> headerParams, int methode, Context context){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        Log.d(TAG, "method LoginRequestCalled");
        String custURL = url + urlExtension;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        StringRequest stringRequest = new StringRequest(methode, custURL,
                response -> {
                    Log.d(TAG, response.toString());
                    if(response.toString().equals( "accepted")){
                        sp.edit().putString(String.valueOf(R.string.login_status), "logged_in");
                        Log.d(TAG, "sp edited _ logged in");
                    }
                     else if(response.toString().equals( "password or email incorrect")){
                        Toast.makeText(context, "password or email incorrect", Toast.LENGTH_SHORT).show();
                        sp.edit().putString(String.valueOf(R.string.login_status),"not_logged_in");
                    }
                    else{
                        Log.d(TAG, response.toString());
                        sp.edit().putString(String.valueOf(R.string.login_status),response.toString());
                        Toast.makeText(context, "Login Error", Toast.LENGTH_SHORT).show();

                    }
                }, error -> {
                    String message = context.getString(R.string.serverErrorMessage);
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    sp.edit().putString(String.valueOf(R.string.login_status), "not_logged_in");

                    Log.d(TAG, "sp edited _ not logged in");
                    Log.d(TAG, error.getMessage());
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                return headerParams;
            }

        };
        requestQueue.add(stringRequest);
    }

}
