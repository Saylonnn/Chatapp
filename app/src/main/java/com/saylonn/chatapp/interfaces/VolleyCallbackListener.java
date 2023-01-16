package com.saylonn.chatapp.interfaces;

import org.json.JSONObject;

public interface VolleyCallbackListener {
    public void callbackMethod(String function, String message);
    public void jsonCallbackMethod(String function, JSONObject json);
}
