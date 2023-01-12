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

import com.saylonn.chatapp.comm.VolleyCallbackListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VolleyRequest {
    private static final String TAG = "CAPP";
    String url = "https://www.api.caylonn.de:1337";
    private List<VolleyCallbackListener> callbackApps = new ArrayList<>();
    MyFirebaseMessagingService fms = new MyFirebaseMessagingService();


    public void login(String email, String password, String token, Context context){
        Log.d(TAG + " VolleyRequest", "login called with "+ email + " " + password + " " + token);
        Map<String, String> headerParams = new HashMap<String, String>();
        headerParams.put("email", email);
        headerParams.put("password", password);
        headerParams.put("token", token);
        doLoginRequest("login", "/auth/login", headerParams, Request.Method.GET, context);

    }

    public void doLoginRequest(String function, String urlExtension, Map<String, String> headerParams, int methode, Context context){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        Log.d(TAG + " VolleyRequest", "method LoginRequestCalled");
        String custURL = url + urlExtension;
        StringRequest stringRequest = new StringRequest(methode, custURL,
                response -> {
                    Log.d(TAG + " VolleyRequest", response.toString());
                    if(response.toString().equals( "accepted")){
                        for(VolleyCallbackListener app : callbackApps){
                            app.callbackMethod("login", "accepted");
                        }
                        Log.d(TAG + " VolleyRequest", "sp edited _ logged in");
                    }
                     else if(response.toString().equals( "password or email incorrect")){
                        Toast.makeText(context, "password or email incorrect", Toast.LENGTH_SHORT).show();
                        Log.d(TAG + " VolleyRequest", response.toString());
                        for(VolleyCallbackListener app : callbackApps){
                            app.callbackMethod("login", "declined");
                        }
                    }
                    else{
                        Log.d(TAG +  " VolleyRequest", response.toString());
                        Toast.makeText(context, "Login Error", Toast.LENGTH_SHORT).show();
                        for(VolleyCallbackListener app : callbackApps){
                            app.callbackMethod("login", "serverError");
                        }
                    }
                }, error -> {
                    String message = context.getString(R.string.serverErrorMessage);
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    Log.d(TAG + " VolleyRequest", "sp edited _ not logged in");
                    Log.d(TAG + " VolleyRequest", error.getMessage());
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                return headerParams;
            }

        };
        requestQueue.add(stringRequest);
    }

    public void addCallbackListener(VolleyCallbackListener listener){
        callbackApps.add(listener);
    }


}
