package com.saylonn.chatapp.ui.chats;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.textfield.TextInputEditText;
import com.saylonn.chatapp.R;
import com.saylonn.chatapp.chathandler.Chat;
import com.saylonn.chatapp.chathandler.ChatDatabase;
import com.saylonn.chatapp.chathandler.Message;
import com.saylonn.chatapp.chathandler.MessageAdapter;
import com.saylonn.chatapp.chathandler.MessageDao;
import com.saylonn.chatapp.comm.VolleyRequest;
import com.saylonn.chatapp.interfaces.VolleyCallbackListener;

import org.json.JSONObject;

import java.util.List;

public class OpenChatActivity extends AppCompatActivity implements VolleyCallbackListener {

    RecyclerView recyclerView;
    Button sendButton;
    TextInputEditText messageField;
    List<Message> messageList;


    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.fragment_open_chat);

        String chatEmail = this.getIntent().getExtras().getString("chatEmail");

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

        VolleyRequest volleyRequest = new VolleyRequest();
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message("localUser", messageField.getText().toString().trim(), chatEmail);
                messageDao.insert(message);
                messageList.add(message);

                MessageAdapter adapter = new MessageAdapter(messageList);
                recyclerView.setAdapter(adapter);
            }
        });



    }

    @Override
    public void callbackMethod(String function, String message) {

    }

    @Override
    public void jsonCallbackMethod(String function, JSONObject json) {

    }
}
