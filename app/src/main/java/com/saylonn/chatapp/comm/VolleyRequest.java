package com.saylonn.chatapp.comm;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.saylonn.chatapp.LoginActivity;
import com.saylonn.chatapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VolleyRequest {
    private static final String TAG = "CAPP";
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

    public void register(String username, String email, String password, String token, Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(String.valueOf(R.string.login_status), "not_tried").apply();
        Log.d(TAG, "login called with "+ email + " " + password + " " + token);
        Map<String, String> headerParams = new HashMap<>();
        headerParams.put("username", username);
        headerParams.put("email", email);
        headerParams.put("password", password);
        headerParams.put("token", token);
        doStringRequest("register", "/auth/register", headerParams, Request.Method.GET, context);
    }

    public void doStringRequest(String function, String urlExtension, Map<String, String> headerParams, int method, Context context){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String custURL = url + urlExtension;
        StringRequest stringRequest = new StringRequest(method, custURL,
                response -> {
                    Log.d(TAG, "String Request Response: " + response);
                    handleResponse(function, response, context);
                },error -> {
                    Log.d(TAG, "String Request Response: " + error.toString());
                    handleError(function, error, context);
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headerParams;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void handleResponse(String function, String message, Context context){
        if(function.equals("login")){
            //read login server response
            if (message.equals("accepted")){
                String text = String.valueOf(R.string.login_status_logged_in);
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                doCallback(function, message);
            }
            else if(message.equals("no password or email")){
                String text = String.valueOf(R.string.login_error_empty_field);
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                doCallback(function, "declined");
            }
            else if(message.equals("password or email incorrect")){
                String text = String.valueOf(R.string.login_status_declined);
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                doCallback(function, "declined");
            }
            else{
                String text = String.valueOf(R.string.login_status_declined);
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                doCallback(function, "declined");
            }
        }
        else if(function.equals("register")){
            if(message.equals("accepted")){
                String text = String.valueOf(String.valueOf(R.string.account_created));
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                doCallback(function, message);
            }
            else if(message.equals("invalid entry")){
                String text = String.valueOf(String.valueOf(R.string.faulty_input));
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                doCallback(function, "declined");
            }
            else if(message.equals("invalid pw length")){
                String text = String.valueOf(String.valueOf(R.string.correct_password_length));
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                doCallback(function, "declined");
            }
            else if(message.equals("email not excepted")){
                String text = String.valueOf(String.valueOf(R.string.email_already_in_use));
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                doCallback(function, "declined");
            }
            else{
                String text = String.valueOf(R.string.login_status_declined);
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                doCallback(function, "declined");
            }
        }
    }

    private void handleError(String function, VolleyError error, Context context){
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
