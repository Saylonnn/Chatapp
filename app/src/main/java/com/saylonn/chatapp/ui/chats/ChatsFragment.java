package com.saylonn.chatapp.ui.chats;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.saylonn.chatapp.R;
import com.saylonn.chatapp.chathandler.Chat;
import com.saylonn.chatapp.chathandler.ChatAdapter;
import com.saylonn.chatapp.chathandler.ChatDao;
import com.saylonn.chatapp.chathandler.ChatDatabase;
import com.saylonn.chatapp.interfaces.VolleyCallbackListener;
import com.saylonn.chatapp.databinding.FragmentChatsBinding;

import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

/**
 * Chat Fragment wo alle offenen Chats aufgelistet werden
 */
public class ChatsFragment extends Fragment implements VolleyCallbackListener {
    private FragmentChatsBinding binding;

    private final String TAG = "CAPP";
    private SharedPreferences encrpytedSharedPreferences;
    private String masterKeyAlias = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentChatsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //lädt die verschlüsselten Shared Preferences -> aufrufen des Passwortes mit encryptedSharedPreferences.getString(.....
        try {
            masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            encrpytedSharedPreferences = EncryptedSharedPreferences.create(
                    "secret_shared_prefs",
                    masterKeyAlias,
                    getActivity(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Alle Chats werden in einem RecylerView aus der ChatDatenbak (Chat Tabelle) geladen
        RecyclerView recyclerView = root.findViewById(R.id.chat_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        ChatDatabase chatDatabase = Room.databaseBuilder(getContext(), ChatDatabase.class, "ChatDatabase")
                .allowMainThreadQueries().build();
        ChatDao chatDao = chatDatabase.chatDao();

        List<Chat> chatList = chatDao.getAllChats();

        ChatAdapter adapter = new ChatAdapter(chatList);
        recyclerView.setAdapter(adapter);

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setUpChatsModels(){

    }

    @Override
    public void callbackMethod(String function, String message) {
        if(function.equals("send_message")){
            if(message.equals("accepted"));
            Toast.makeText(getActivity(), "Nachricht versandt", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void jsonCallbackMethod(String function, JSONObject json) {

    }

}