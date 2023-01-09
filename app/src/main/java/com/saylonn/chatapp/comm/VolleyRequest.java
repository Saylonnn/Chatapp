package com.saylonn.chatapp.comm;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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
    private List<CallbackInterface> callbackApps = new ArrayList<>();
    RequestQueue queue;
    String url = "https://www.api.caylonn.de:1337";
    Context context;
    MyFirebaseMessagingService fms = new MyFirebaseMessagingService();

    public VolleyRequest(Context context){
        this.context = context;
        queue = Volley.newRequestQueue(context);
    }

    public void login(String email, String password){
        Log.d(TAG, "login called with "+ email + " " + password);
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //String token = sharedPreferences.getString("token_key", "none");
        ///Log.d(TAG, "token: "+ token);
        Map<String, String> headerParams = new HashMap<String, String>();
        headerParams.put("email", email);
        headerParams.put("password", password);
        //headerParams.put("token", token);
        doStringRequest("login", "/auth/login", headerParams, Request.Method.GET);
    }

    public void doStringRequest(String function, String urlExtension, Map<String, String> headerParams, int methode){
        Log.d(TAG, "method doStringRequestCalled");
        String custURL = url + urlExtension;
        StringRequest stringRequest = new StringRequest(methode, custURL,
                response -> {
                    for (CallbackInterface x : callbackApps) {
                        x.callbackFunction(function, response.toString());

                    }
                }, error -> {
                    String message = context.getString(R.string.serverErrorMessage);
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                    for (CallbackInterface x : callbackApps){
                        x.callbackFunction(function, error.getMessage());
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                return headerParams;
            }

        };
        queue.add(stringRequest);
    }



    public void addCallbackListener(CallbackInterface ma){
        callbackApps.add(ma);
    }
}
