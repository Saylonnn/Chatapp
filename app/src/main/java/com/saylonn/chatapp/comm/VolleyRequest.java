package com.saylonn.chatapp.comm;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
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
        doStringRequest("login", "/auth/login", headerParams, Request.Method.GET, context);
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
        doStringRequest("register", "/auth/login", headerParams, Request.Method.GET, context);
    }

    public void doStringRequest(String function, String urlExtension, Map<String, String> headerParams, int method, Context context){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String custURL = url + urlExtension;
        StringRequest stringRequest = new StringRequest(method, custURL,
                response -> {
                    Log.d(TAG, "String Request Response: " + response);
                    handleResponse(function, response);
                },
                error -> {
                    Log.d(TAG, "String Request Response: " + error.toString());
                    handleError(function, error);
                });
    }

    private void handleResponse(String function, String message){
        if(function.equals("login")){
            //read login server response
            if (message.equals("accepted")){
                doCallback(function, message);
            }
            else if(message.equals("no password or email")){
                doCallback(function, "declined");
            }
            else if(message.equals("password or email incorrect")){
                doCallback(function, "declined");
            }
            else{
                doCallback(function, "declined");
            }
        }
        else if(function.equals("register")){
            //read register server response
        }
    }

    private void handleError(String function, VolleyError error){
        //handle all Error responses
        Log.d(TAG, "Error Message: " + error.networkResponse.toString());
        String resp = error.networkResponse.toString();
        if(resp.equals("server error")){
            doCallback(function, "server Error");
        }
    }

    private void doCallback(String function, String message){
        for(VolleyCallbackListener vCL : callbackApps){
            vCL.callbackMethod(function, message);
        }
    }

    //TODO: Updated Token to server
    //      Register new account
    //      sendMessage


    public void addCallbackListener(VolleyCallbackListener ma){
        callbackApps.add(ma);
    }

}
