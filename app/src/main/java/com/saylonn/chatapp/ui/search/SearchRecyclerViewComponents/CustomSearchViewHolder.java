package com.saylonn.chatapp.ui.search.SearchRecyclerViewComponents;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.saylonn.chatapp.R;
import com.saylonn.chatapp.chathandler.Chat;
import com.saylonn.chatapp.chathandler.ChatDao;
import com.saylonn.chatapp.chathandler.ChatDatabase;

public class CustomSearchViewHolder extends RecyclerView.ViewHolder{
    private final String TAG = "CAPP";
    public TextView textUsername, textEmail;
    public ImageButton contactButton;


    public CustomSearchViewHolder(@NonNull View itemView){
            super(itemView);

            textUsername = itemView.findViewById(R.id.contact_name);
            textEmail = itemView.findViewById(R.id.contact_email);
            contactButton = itemView.findViewById(R.id.searchRcButton);


            itemView.findViewById(R.id.searchRcButton).setOnClickListener(v -> {
                saveUser();
            });

    }

    private void saveUser() {
        ChatDatabase chatDatabase = Room.databaseBuilder(itemView.getContext(), ChatDatabase.class, "ChatDatabase")
                .allowMainThreadQueries().build();
        ChatDao chatDao = chatDatabase.chatDao();

        String username = textUsername.getText().toString().trim();
        String email = textEmail.getText().toString().trim();


        Chat chat = new Chat(username, email, "");
        chatDao.insert(chat);

    }

}

