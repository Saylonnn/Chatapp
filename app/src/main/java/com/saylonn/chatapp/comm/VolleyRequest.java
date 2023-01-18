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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.saylonn.chatapp.R;
import com.saylonn.chatapp.interfaces.VolleyCallbackListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Volley Request stellt funktionen zur Verfügung und ruft anschließend die dazugehörigen Webrequests auf
 */
public class VolleyRequest {
    private static final String TAG = "CAPP";
    String url = "https://www.api.caylonn.de:1337";
    final private List<VolleyCallbackListener> callbackApps = new ArrayList<>();

    /**
     * prüft ob die angegeben daten von der API akzeptiert werden
     * @param email - des Nutzers, in SharedPreferences gespeichert
     * @param password- des Nutzers, in EncryptedSharedPreferences gespeichert
     * @param token- token der App, in SharedPreferences gespeichert
     * @param context - Context der Aufrufenden Activity - wichtig da VolleyRequest selber nicht auf den Context zugreifen kann
     */
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

    /**
     * zum Registrieren bei der API
     * @param username - angegeben durch den USER
     * @param email - angegeben durch den USER
     * @param password - angegeben durch den USER
     * @param token  - token der App, in SharedPreferences gespeichert
     * @param context - Context der Aufrufenden Activity - wichtig da VolleyRequest selber nicht auf den Context zugreifen kann
     */
    public void register(String username, String email, String password, String token, Context context){
        //SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        //sp.edit().putString(String.valueOf(R.string.login_status), "not_tried").apply();
        Log.d(TAG, "login called with "+ email + " " + password + " " + token);
        Map<String, String> headerParams = new HashMap<>();
        headerParams.put("username", username);
        headerParams.put("email", email);
        headerParams.put("password", password);
        headerParams.put("token", token);
        doStringRequest("register", "/auth/register", headerParams, Request.Method.GET, context);
    }

    /**
     * fragt nach einer Liste von Usern die die angebene Buchstabenfolge beinhalten, kann ohne Credentials aufgerufen werden
     * @param searchString - zu suchender String
     * @param context - Context der Aufrufenden Activity - wichtig da VolleyRequest selber nicht auf den Context zugreifen kann
     */
    public void listUsers(String searchString, Context context){
        Log.d(TAG, "list_users called with search String : " + searchString);
        Map<String, String> headerParams = new HashMap<>();
        headerParams.put("search_string", searchString);
        doJsonRequest("list_users", "/contacts/list_users", headerParams, Request.Method.GET, context);
    }

    /**
     * benachricht die API eine Nachricht senden zu wollen
     * @param sourceEmail - email des Senders - in SharedPreferences gespeichert
     * @param password - passwort des Senders - in EncryptedSharedPreferences gespeichert
     * @param token - token der App - in SharedPreferences gespeichert
     * @param targetEmail - adressierte Email - durch USER gegeben
     * @param message - zu übermittelnde Nachricht
     * @param context - Context der Aufrufenden Activity - wichtig da VolleyRequest selber nicht auf den Context zugreifen kann
     */
    public void sendMessage(String sourceEmail, String password, String token, String targetEmail, String message, Context context){
        Log.d(TAG, "send_message called with email: " + targetEmail + " , message: " + message + " from email: " + sourceEmail + " with password: " + password);
        Map<String, String> headerParams = new HashMap<>();
        headerParams.put("email", sourceEmail);
        headerParams.put("password", password   );
        headerParams.put("target_email", targetEmail);
        headerParams.put("message", message);
        headerParams.put("token", token);
        doStringRequest("sendMessage", "/messaging/send_message", headerParams, Request.Method.POST, context);
    }

    /**
     * Übermittelt die JSON anfrage an das VolleyFramework
     * @param function - in welcher function diese Methode aufgerufen wird (login, list_users, ...) - wird weitergereicht
     * @param urlExtension - String der nach der Domain geschrieben wird damit es die api auf der richtigen adresse erhält
     * @param headerParams - Parameter die in den Head geschrieben werden als Map, Credentials usw.
     * @param method - Integer: Beschreibt die HTML Anfrage Methode POST, GET, ... als Request.Method.POST
     * @param context - Context der Aufrufenden Activity - wichtig da VolleyRequest selber nicht auf den Context zugreifen kann
     */
    private void doJsonRequest(String function, String urlExtension, Map<String, String> headerParams, int method, Context context){
        Log.d(TAG, "VolleyRequest doJsonRequest");
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String custURL = url + urlExtension;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(custURL,
                response -> {
                    Log.d(TAG, "JSON Request Response: " + response);
                    handleJSONResponse(function, response, context);
                },error -> {
            Log.d(TAG, "JSON Request ErrorResponse: " + error.toString());
            handleError(function, error, context);
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headerParams;
            }
        };
        requestQueue.add(jsonRequest);
    }

    /**
     * Übermittelt die StringRequst an das VolleyFramework
     * @param function - in welcher function diese Methode aufgerufen wird (login, list_users, ...) - wird weitergereicht
     * @param urlExtension - String der nach der Domain geschrieben wird damit es die api auf der richtigen adresse erhält
     * @param headerParams - Parameter die in den Head geschrieben werden als Map, Credentials usw.
     * @param method - Integer: Beschreibt die HTML Anfrage Methode POST, GET, ... als Request.Method.POST
     * @param context - Context der Aufrufenden Activity - wichtig da VolleyRequest selber nicht auf den Context zugreifen kann
     */
    private void doStringRequest(String function, String urlExtension, Map<String, String> headerParams, int method, Context context){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String custURL = url + urlExtension;
        StringRequest stringRequest = new StringRequest(method, custURL,
                response -> {
                    Log.d(TAG, "String Request Response: " + response);
                    handleResponse(function, response, context);
                },error -> {
                    Log.d(TAG, "String Request ErrorResponse: " + error.toString());
                    handleError(function, error, context);
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headerParams;
            }
        };
        requestQueue.add(stringRequest);
    }

    /**
     * handelt die Serverantwort nach einer StringRequest
     * @param function - in welcher function diese Methode aufgerufen wird (login, list_users, ...) - wird weitergereicht
     * @param message - Antwortnachricht des Servers
     * @param context - Context der Aufrufenden Activity - wichtig da VolleyRequest selber nicht auf den Context zugreifen kann
     */
    private void handleResponse(String function, String message, Context context){
        if(function.equals("login")){
            //read login server response
            if (message.equals("accepted")){
                String text = context.getString(R.string.login_status_logged_in);
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                doCallback(function, message);
            }
            else if(message.equals("no password or email")){
                String text = context.getString(R.string.login_error_empty_field);
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                doCallback(function, "declined");
            }
            else if(message.equals("password or email incorrect")){
                String text = context.getString(R.string.login_status_declined);
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                doCallback(function, "declined");
            }
            else{
                String text = context.getString(R.string.login_status_declined);
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                doCallback(function, "declined");
            }
        }
        else if(function.equals("register")){
            if(message.equals("registered")){
                String text = context.getString(R.string.account_created);
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                doCallback(function, "accepted");
            }
            else if(message.equals("invalid entry")){
                String text = context.getString(R.string.faulty_input);
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                doCallback(function, "declined");
            }
            else if(message.equals("invalid pw length")){
                String text = context.getString(R.string.correct_password_length);
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                doCallback(function, "declined");
            }
            else if(message.equals("email not excepted")){
                String text = context.getString(R.string.email_already_in_use);
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                doCallback(function, "declined");
            }
            else{
                String text = context.getString(R.string.login_status_declined);
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                doCallback(function, "declined");
            }
        }
        else if(function.equals("sendMessage")){
            Log.d(TAG, "[VolleyRequest]{handleResponse}: " + message);
        }
    }

    /**
     * handelt die ServerResponse nach einer JsonRequest
     * @param function - in welcher function diese Methode aufgerufen wird (login, list_users, ...) - wird weitergereicht
     * @param json - vom Server übermittelte Antwort
     * @param context - Context der Aufrufenden Activity - wichtig da VolleyRequest selber nicht auf den Context zugreifen kann
     */
    private void handleJSONResponse(String function, JSONObject json, Context context){
        Log.d(TAG, "Handle JSON Response");
        if(function.equals("list_users")){
            for(VolleyCallbackListener vCL : callbackApps){
                vCL.jsonCallbackMethod(function, json);
            }
        }
    }

    /**
     * Handelt den Fehlercode aller Webrequests
     * @param function - in welcher function diese Methode aufgerufen wird (login, list_users, ...) - wird weitergereicht
     * @param error - Fehler der WebRequest
     * @param context - Context der Aufrufenden Activity - wichtig da VolleyRequest selber nicht auf den Context zugreifen kann
     */
    private void handleError(String function, VolleyError error, Context context){
        //handle all Error responses

        Log.d(TAG, "Error Message: " + error.toString());
        String resp = error.toString();
        if(resp.equals("com.android.volley.TimeoutError")){
            Toast.makeText(context, "Server nicht erreichbar", Toast.LENGTH_SHORT).show();
        }
        if(resp.equals("server error")){
            doCallback(function, "server Error");
        }
        if(error.getMessage() != null){
            if(error.getMessage().equals("cannot find target adress"));
            Toast.makeText(context, String.valueOf(R.string.target_adress_not_available), Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * verteilt die Antwort der auf alle Objekte die das VolleyCallbackListener-Interface implementieren und sich als Observer eingetragen haben
     * @param function - in welcher function diese Methode aufgerufen wird (login, list_users, ...) - wird weitergereicht
     * @param message - die an die Objekte zu übermittelnde Nachricht
     * beide Parameter werden nur übermittelt
     */
    private void doCallback(String function, String message){
        for(VolleyCallbackListener vCL : callbackApps){
            vCL.callbackMethod(function, message);
        }
    }

    //TODO: Updated Token to server
    //      Register new account
    //      sendMessage

    /**
     * Damit kann sich ein beliebiges Objekt was das VolleyCallbackListener-Interface implementiert als Observer eintragen
     * @param ma - Das objekt was sich als Observer eintragen will
     */
    public void addCallbackListener(VolleyCallbackListener ma){
        callbackApps.add(ma);
    }

}
