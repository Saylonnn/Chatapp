package com.saylonn.chatapp.ui.SearchRecyclerViewComponents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saylonn.chatapp.R;

import java.util.List;

public class CustomSearchAdapter extends RecyclerView.Adapter<CustomSearchViewHolder> {
    private Context context;
    private List<Contact> list;

    public CustomSearchAdapter(Context context, List<Contact> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CustomSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomSearchViewHolder(LayoutInflater.from(context).inflate(R.layout.search_contact_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomSearchViewHolder holder, int position) {
        holder.textUsername.setText(list.get(position).getUsername());
        holder.textEmail.setText(list.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
