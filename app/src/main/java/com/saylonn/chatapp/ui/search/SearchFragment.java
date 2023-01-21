package com.saylonn.chatapp.ui.search;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.saylonn.chatapp.interfaces.VolleyCallbackListener;
import com.saylonn.chatapp.comm.VolleyRequest;
import com.saylonn.chatapp.databinding.FragmentSearchBinding;
import com.saylonn.chatapp.ui.search.SearchRecyclerViewComponents.Contact;
import com.saylonn.chatapp.ui.search.SearchRecyclerViewComponents.CustomSearchAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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

        volleyRequest = new VolleyRequest();
        volleyRequest.addCallbackListener(this);
        searchView = binding.searchView;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                volleyRequest.listUsers(query, getActivity());
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

    /**
     * Hier nicht nötig auszuführen da das SearchFragment nie eine StringRequest ausführt
     * Callbackmethode für die Asynchrone StringWebRequest
     * @param function übergibt den Namen der funktion die in der volleyRequst Klasse aufgerufen wurde
     *                 daran kann die Callbackmethode entscheiden ob sie etwas tun muss oder nicht
     * @param message ergebnis der Webrequest
     */
    @Override
    public void callbackMethod(String function, String message) {

    }

    /**
     * Callbackmethode für die Asynchrone JsonWebRequest
     * @param function übergibt den Namen der funktion die in der volleyRequst Klasse aufgerufen wurde
     *                 daran kann die Callbackmethode entscheiden ob sie etwas tun muss oder nicht
     * @param json ergebnis der JsonWebrequest
     */
    @Override
    public void jsonCallbackMethod(String function, JSONObject json) {
        if(function.equals("list_users")){
            displayItems(json);
        }

    }

    /**
     * Zeigt alle Elemente des übergebenen JsonObject als Listenelement im RecyclerView an
     * @param json - JSONObjekt mit allen Usern die den gesuchten String enthalten
     */
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