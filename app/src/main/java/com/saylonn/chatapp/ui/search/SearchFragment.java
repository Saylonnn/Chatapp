package com.saylonn.chatapp.ui.search;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.saylonn.chatapp.R;
import com.saylonn.chatapp.comm.VolleyCallbackListener;
import com.saylonn.chatapp.comm.VolleyRequest;
import com.saylonn.chatapp.databinding.FragmentSearchBinding;
import com.saylonn.chatapp.ui.SearchRecyclerViewComponents.Contact;
import com.saylonn.chatapp.ui.SearchRecyclerViewComponents.CustomSearchAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SearchFragment extends Fragment implements VolleyCallbackListener {
    private VolleyRequest volleyRequest;
    private FragmentSearchBinding binding;
    private SearchView searchView;
    private final String TAG = "CAPP";
    RecyclerView recyclerView;
    List<Contact> myContactList;
    CustomSearchAdapter customSearchAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SearchViewModel searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Searchleiste
        //https://developer.android.com/develop/ui/views/search/search-dialog

        //Liste an usern anzeigen mittels: RecyclerView

        //final TextView textView = binding.textSearch;
        //searchViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        volleyRequest = new VolleyRequest();
        volleyRequest.addCallbackListener(this);
        searchView = binding.searchView;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                volleyRequest.list_users(query, getActivity());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void callbackMethod(String function, String message) {

    }

    @Override
    public void jsonCallbackMethod(String function, JSONObject json) {
        if(function.equals("list_users")){
            displayItems(json);
        }

    }

    private void displayItems(JSONObject json){
        recyclerView = binding.searchRcView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        myContactList = new ArrayList<>();

        json.keys().forEachRemaining(key -> {
            JSONObject value = null;
            try {
                value = (JSONObject) json.get(key);
                myContactList.add(new Contact(value.get("username").toString(), value.get("email").toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "Key: " + value);
        });

        customSearchAdapter = new CustomSearchAdapter(getActivity(), myContactList);
        recyclerView.setAdapter(customSearchAdapter);

    }
}