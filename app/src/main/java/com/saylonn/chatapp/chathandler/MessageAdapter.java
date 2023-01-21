package com.saylonn.chatapp.chathandler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saylonn.chatapp.R;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {
    private List<Message> messages = new ArrayList<>();

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if(viewType == 1){
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.my_message_row, parent, false);
        }
        else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.other_message_row, parent, false);
        }

        return new MessageHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        Message currentMessage = messages.get(position);
        holder.textViewMessage.setText(currentMessage.getMessage());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public MessageAdapter(List<Message> messages) {
        this.messages = messages;
    }

    class MessageHolder extends RecyclerView.ViewHolder {
        private TextView textViewMessage;

        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.message_txt);
        }
    }

    @Override
    public int getItemViewType(int position) {
        String sender = messages.get(position).getSender();
        if(sender.equals("localUser")){
            return 1;
        }
        return 0;
    }
}
