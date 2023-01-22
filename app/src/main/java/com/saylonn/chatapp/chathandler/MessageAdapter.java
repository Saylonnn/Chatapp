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

/**
 * Adapter um die Chats in einer RecyclerView Liste anzeigen zu können
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {
    private List<Message> messages = new ArrayList<>();

    /**
     * Beim Erstellen der OpenChatActivity wird der Adapter geladen
     * @param viewType der viewType den man aus der getItemViewType Methode bekommt
     *                 Je nachdem wer die Nachricht gesendet hat, wird eine andere Layout Datei verwendet
     * @return Gibt den MessageHolder zurück
     */
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

    /**
     * Eine Methode um den Sender zu bekommen
     * @param position Die jeweilige Nachricht ein einer bestimmten Position
     * @return Wenn der Sender der localUser ist (also wenn man eine Nachricht sendet) wir eine 1 returned, sonst eine 0
     *         Wird in der onCreate Methode verwendet
     */
    @Override
    public int getItemViewType(int position) {
        String sender = messages.get(position).getSender();
        if(sender.equals("localUser")){
            return 1;
        }
        return 0;
    }
}
