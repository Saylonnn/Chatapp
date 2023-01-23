package com.saylonn.chatapp.ui.chats;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import com.saylonn.chatapp.chathandler.MessageDao;

import com.saylonn.chatapp.interfaces.VolleyCallbackListener;
import com.saylonn.chatapp.databinding.FragmentChatsBinding;

import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

/**
 * Chat Fragment wo alle offenen Chats aufgelistet werden
 */
public class ChatsFragment extends Fragment implements VolleyCallbackListener {
    private FragmentChatsBinding binding;

    private final String TAG = "CAPP";
    private SharedPreferences encrpytedSharedPreferences;
    private String masterKeyAlias = null;
    RecyclerView recyclerView;


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
        recyclerView = root.findViewById(R.id.chat_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        ChatDatabase chatDatabase = Room.databaseBuilder(getContext(), ChatDatabase.class, "ChatDatabase")
                .allowMainThreadQueries().build();
        ChatDao chatDao = chatDatabase.chatDao();

        List<Chat> chatList = chatDao.getAllChats();

        ChatAdapter adapter = new ChatAdapter(chatList);
        recyclerView.setAdapter(adapter);

        // Wenn man einen Chat na links swiped, wird der Eintrag gelöscht
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT){
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                MessageDao messageDao = chatDatabase.messageDao();
                messageDao.deleteWhereEmail(adapter.getChatAt(viewHolder.getAdapterPosition()).getEmail());
                chatDao.delete(adapter.getChatAt(viewHolder.getAdapterPosition()));

                updateChats();
            }

        }).attachToRecyclerView(recyclerView);

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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

    /**
     * Lädt die Chat Einträge aus der Datenbank und reloaded den RecycleViewer
     */
    public void updateChats() {
        ChatDatabase chatDatabase = Room.databaseBuilder(getContext(), ChatDatabase.class, "ChatDatabase")
                .allowMainThreadQueries().build();
        ChatDao chatDao = chatDatabase.chatDao();
        List <Chat> chatList = chatDao.getAllChats();
        ChatAdapter chatAdapter = new ChatAdapter(chatList);
        recyclerView.setAdapter(chatAdapter);
    }

    /**
     * BroadcastReceiver der benachrichtig wird, wenn eine neue Nachricht empfangen wird und ruft die updateChats() Methode auf
     */
    public BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateChats();
        }
    };
    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(myReceiver, new IntentFilter("FBR-update-chats"));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(myReceiver);
    }

}