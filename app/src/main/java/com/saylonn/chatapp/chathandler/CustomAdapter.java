package com.saylonn.chatapp.chathandler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saylonn.chatapp.R;
import com.saylonn.chatapp.ui.chats.OpenChatActivity;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<String> message_id, sender, message;

    public CustomAdapter(Context context, ArrayList message_id, ArrayList sender, ArrayList message) {
        this.context = context;
        this.message_id = message_id;
        this.sender = sender;
        this.message = message;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_chat_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.message_txt.setText(String.valueOf(message.get(position)));
    }

    @Override
    public int getItemCount() {
        return message.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView message_txt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            message_txt = itemView.findViewById(R.id.message_txt);
        }
    }

}
