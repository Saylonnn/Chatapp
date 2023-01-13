package com.saylonn.chatapp.comm;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.saylonn.chatapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VolleyRequest {
    private static final String TAG = "VolleyRequest";
    String url = "https://www.api.caylonn.de:1337";
    final private List<VolleyCallbackListener> callbackApps = new ArrayList<>();

    public void login(String email, String password, String token, Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(String.valueOf(R.string.login_status), "not_tried").apply();
        Log.d(TAG, "login called with "+ email + " " + password + " " + token);
        Map<String, String> headerParams = new HashMap<>();
        headerParams.put("email", email);
        headerParams.put("password", password);
        headerParams.put("token", token);
        doLoginRequest("/auth/login", headerParams, Request.Method.GET, context);
    }

    public void doLoginRequest(String urlExtension, Map<String, String> headerParams, int methode, Context context){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        Log.d(TAG, "method LoginRequestCalled");
        String custURL = url + urlExtension;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        StringRequest stringRequest = new StringRequest(methode, custURL,
                response -> {
                    Log.d(TAG, response);
                    if(response.equals( "accepted")){
                        for(VolleyCallbackListener cI : callbackApps){
                            cI.callbackMethod("login", "accepted");
                        }
                        Log.d(TAG, "sp edited _ logged in");
                    }
                     else if(response.equals( "password or email incorrect")){
                        Toast.makeText(context, "password or email incorrect", Toast.LENGTH_SHORT).show();
                        for(VolleyCallbackListener cI : callbackApps){
                            cI.callbackMethod("login", "not_accepted");
                        }
                    }
                    else{
                        Log.d(TAG, response);
                        for(VolleyCallbackListener cI : callbackApps){
                            cI.callbackMethod("login", "not_accepted");
                        }
                        Toast.makeText(context, "Login Error", Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
                    String message = context.getString(R.string.serverErrorMessage);
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    sp.edit().putString(String.valueOf(R.string.login_status), "not_logged_in").apply();

                    Log.d(TAG, "sp edited _ not logged in");
                    Log.d(TAG, error.getMessage());
                }){
        };
        requestQueue.add(stringRequest);
    }

    public void register(String email, String password, String token, Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(String.valueOf(R.string.login_status), "not_tried").apply();
        Log.d(TAG, "login called with "+ email + " " + password + " " + token);
        Map<String, String> headerParams = new HashMap<>();
        headerParams.put("email", email);
        headerParams.put("password", password);
        headerParams.put("token", token);
        doRegisterRequest( "/auth/login", headerParams, Request.Method.GET, context);
    }

    private void doRegisterRequest(String urlExtension, Map<String, String> headerParams, int methode, Context context){

    }



    //TODO: Updated Token to server
    //      Register new account
    //      sendMessage


    public void addCallbackListener(VolleyCallbackListener ma){
        callbackApps.add(ma);
    }

}
