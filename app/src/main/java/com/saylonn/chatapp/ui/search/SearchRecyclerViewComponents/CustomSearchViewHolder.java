package com.saylonn.chatapp.ui.search.SearchRecyclerViewComponents;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saylonn.chatapp.R;

public class CustomSearchViewHolder extends RecyclerView.ViewHolder{
    public TextView textUsername, textEmail;
    public Button contactButton;

    public CustomSearchViewHolder(@NonNull View itemView){
            super(itemView);
            textUsername = itemView.findViewById(R.id.contact_name);
            textEmail = itemView.findViewById(R.id.contact_email);
            contactButton = itemView.findViewById(R.id.searchRcButton);

    }
}

