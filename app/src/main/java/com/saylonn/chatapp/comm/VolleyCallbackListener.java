package com.saylonn.chatapp.comm;

import org.json.JSONObject;

public interface VolleyCallbackListener {
    public void callbackMethod(String function, String message);
    public void jsonCallbackMethod(String function, JSONObject json);
}
