package com.saylonn.chatapp.chathandler;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.FractionRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.FragmentKt;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.saylonn.chatapp.R;
import com.saylonn.chatapp.ui.chats.OpenChatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder> {
    private List<Chat> chats = new ArrayList<>();

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_row_all_chats, parent, false);
        return new ChatHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {
        Chat currentChat = chats.get(position);
        holder.textViewUsername.setText(currentChat.getUsername());
        holder.textViewLastMessage.setText(currentChat.getLast_msg());

        holder.itemView.setOnClickListener(view -> {
            Context context = view.getContext();

            Intent intent = new Intent(context, OpenChatActivity.class);
            intent.putExtra("chatEmail", String.valueOf(currentChat.getEmail()));

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public ChatAdapter(List<Chat> chats) {
        this.chats = chats;
    }

    class ChatHolder extends RecyclerView.ViewHolder {
        private TextView textViewUsername;
        private TextView textViewLastMessage;

        public ChatHolder(@NonNull View itemView) {
            super(itemView);
            textViewUsername = itemView.findViewById(R.id.username_txt);
            textViewLastMessage = itemView.findViewById(R.id.last_message_txt);
        }
    }
}
