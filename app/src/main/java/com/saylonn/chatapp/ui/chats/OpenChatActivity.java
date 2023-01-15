package com.saylonn.chatapp.ui.chats;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.saylonn.chatapp.R;
import com.saylonn.chatapp.chathandler.ChatDatabase;
import com.saylonn.chatapp.chathandler.CustomAdapter;

import java.util.ArrayList;

public class OpenChatActivity extends AppCompatActivity {

    ChatDatabase chatDatabase;
    ArrayList<String> message_id;
    ArrayList<String> sender;
    ArrayList<String> message;
    CustomAdapter customAdapter;

    RecyclerView recyclerView;
    Button sendButton;
    TextInputEditText messageField;

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.fragment_open_chat);

        recyclerView = findViewById(R.id.openChatRecyclerView);
        sendButton = findViewById(R.id.button_send_message);
        messageField = findViewById(R.id.messageInputField);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        chatDatabase = new ChatDatabase(OpenChatActivity.this);
        message_id = new ArrayList<>();
        sender = new ArrayList<>();
        message = new ArrayList<>();

        storeDataInArrays();

        customAdapter = new CustomAdapter(OpenChatActivity.this, message_id, sender, message);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(OpenChatActivity.this));

    }

    void sendMessage() {
        chatDatabase.addLocalMessage("localUser", messageField.getText().toString().trim());
        messageField.setText("");
    }

    void storeDataInArrays() {
        Cursor cursor = chatDatabase.readAllData();

        if(cursor.getCount() == 0){
            Toast.makeText(this, "There is no Data.", Toast.LENGTH_SHORT).show();
        } else {
            while(cursor.moveToNext()){
                message_id.add(cursor.getString(0));
                sender.add(cursor.getString(1));
                message.add(cursor.getString(2));
            }
        }
    }


    public ArrayList<String> getSender() {
        return sender;
    }
}
