package com.saylonn.chatapp.ui.search.SearchRecyclerViewComponents;

import android.app.Activity;
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

    /**
     * Hauptkonstruktor
     * @param context - übergibt einen Context an das Objekt mit dem es arbeiten kann
     * @param list - eine Liste mit Contact objekten - aus diesen werden dann die Elemente der Liste erzeugt
     */
    public CustomSearchAdapter(Context context, List<Contact> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CustomSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomSearchViewHolder(LayoutInflater.from(context).inflate(R.layout.search_contact_item, parent, false));
    }

    /**
     * weist jedem Objekt im RecyclerView einen Username und eine Email zu abhängig von der Position
     * @param holder - ViewHolder in dem die Elemente der Liste angezeigt werden sollen
     * @param position - gibt an, an welcher Position sich das Element befindet
     */
    @Override
    public void onBindViewHolder(@NonNull CustomSearchViewHolder holder, int position) {
        holder.textUsername.setText(list.get(position).getUsername());
        holder.textEmail.setText(list.get(position).getEmail());
    }

    /**
     *
     * @return gibt die Größe der Liste mit den Contact Objekten zurück
     */
    @Override
    public int getItemCount() {
        return list.size();
    }
}
