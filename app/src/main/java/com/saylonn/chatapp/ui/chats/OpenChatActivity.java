package com.saylonn.chatapp.ui.chats;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.google.android.material.textfield.TextInputEditText;
import com.saylonn.chatapp.R;
import com.saylonn.chatapp.chathandler.Chat;
import com.saylonn.chatapp.chathandler.ChatAdapter;
import com.saylonn.chatapp.chathandler.ChatDatabase;
import com.saylonn.chatapp.chathandler.Message;
import com.saylonn.chatapp.chathandler.MessageAdapter;
import com.saylonn.chatapp.chathandler.MessageDao;
import com.saylonn.chatapp.comm.VolleyRequest;
import com.saylonn.chatapp.interfaces.VolleyCallbackListener;

import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;

/**
 * Activity die geöffnet wird, wenn man in dem ChatsFragment auf einen Chat klickt
 */
public class OpenChatActivity extends AppCompatActivity implements VolleyCallbackListener {

    RecyclerView recyclerView;
    Button sendButton;
    TextInputEditText messageField;
    String chatEmail;
    List<Message> messageList;
    VolleyRequest volleyRequest;
    private final String TAG = "CAPP";
    private SharedPreferences encryptedSP;
    private String masterKeyAlias = null;



    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.fragment_open_chat);

        // Man bekommt das Intent mit der Email auf die man geklickt hat, damit die dazugehörigen Nachrichten geladen werden
        chatEmail = this.getIntent().getExtras().getString("chatEmail");

        recyclerView = findViewById(R.id.openChatRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        ChatDatabase chatDatabase = Room.databaseBuilder(this, ChatDatabase.class, "ChatDatabase")
                .allowMainThreadQueries().build();
        MessageDao messageDao = chatDatabase.messageDao();

        sendButton = findViewById(R.id.button_send_message);
        messageField = findViewById(R.id.messageInputField);

        messageList = messageDao.getAllMessages(chatEmail);

        MessageAdapter adapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(adapter);

        if(messageList.size() > 0) {
            recyclerView.smoothScrollToPosition(messageList.size() - 1);
        }

        // Wird zum Senden der Nachrichten benötigt
        try{
            masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            encryptedSP = EncryptedSharedPreferences.create(
                    "secret_shared_prefs",
                    masterKeyAlias,
                    getApplicationContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        volleyRequest = new VolleyRequest();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String sourceEmail = sp.getString(String.valueOf(R.string.login_email), "none");
        String password = encryptedSP.getString(String.valueOf(R.string.login_password), "none");
        String token = sp.getString(String.valueOf(R.string.token_key), "none");

        // Beim klicken auf den Sendebutton wird die Nachricht in die Datenbank gespeichert und mithilfe des VolleyRequests an den entsprechenden User gesendet
        sendButton.setOnClickListener(view -> {
            String messageText = messageField.getText().toString().trim();

            if(!messageText.equals("")){
                Message message = new Message("localUser", chatEmail, messageText);
                messageDao.insert(message);

                Log.d(TAG, "targetEmail: " + chatEmail + " message: " + messageText);
                volleyRequest.sendMessage(sourceEmail, password, token, chatEmail, messageText, getApplicationContext());

                updateMessages();
                messageField.setText("");
                if(messageList.size() > 1){
                    recyclerView.smoothScrollToPosition(messageList.size() - 1);
                }
            }
        });


    }

    @Override
    public void callbackMethod(String function, String message) {
        if(function.equals("send_message")){
            if(message.equals("accepted"));
            Toast.makeText(getApplicationContext(), "Nachricht versandt", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void jsonCallbackMethod(String function, JSONObject json) {

    }

    /**
     * Lädt die Nachrichten Einträge aus der Datenbank und reloaded den RecycleViewer
     */
    public void updateMessages() {
        ChatDatabase chatDatabase = Room.databaseBuilder(this, ChatDatabase.class, "ChatDatabase")
                .allowMainThreadQueries().build();
        MessageDao messageDao = chatDatabase.messageDao();
        List<Message> messageList = messageDao.getAllMessages(chatEmail);
        MessageAdapter messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
    }

    /**
     * BroadcastReceiver der benachrichtig wird, wenn eine neue Nachricht empfangen wird und ruft die updateMessages() Methode auf
     */
    public BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateMessages();
        }
    };
    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(myReceiver, new IntentFilter("FBR-update-chats"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myReceiver);
    }
}
