package com.saylonn.chatapp.ui.chats;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.saylonn.chatapp.R;
import com.saylonn.chatapp.interfaces.VolleyCallbackListener;
import com.saylonn.chatapp.comm.VolleyRequest;
import com.saylonn.chatapp.databinding.FragmentChatsBinding;

import org.json.JSONObject;

import java.util.ArrayList;

public class ChatsFragment extends Fragment implements VolleyCallbackListener {
    private FragmentChatsBinding binding;
    EditText testEmailTv;
    EditText testMessageTv;
    Button sendTestMessageBtn;
    VolleyRequest volleyRequest;
    private final String TAG = "CAPP";


    ArrayList<ChatsModel> chatsModels = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ChatsViewModel chatsViewModel =
                new ViewModelProvider(this).get(ChatsViewModel.class);

        binding = FragmentChatsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        // der abschnitt ist nur zum Testen -> kannst du dann entfernen Karol
        volleyRequest = new VolleyRequest();
        volleyRequest.addCallbackListener(this);
        testEmailTv = binding.testEmailTv;
        testMessageTv = binding.testMessageTv;
        sendTestMessageBtn = binding.sendTestMessageButton;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sourceEmail = sp.getString(String.valueOf(R.string.login_email), "none");
        String password = sp.getString(String.valueOf(R.string.login_password), "none");
        String token = sp.getString(String.valueOf(R.string.token_key), "none");


        sendTestMessageBtn.setOnClickListener(v -> {
            if(testEmailTv.getText().toString().equals("")) {
                testEmailTv.setError("darf nicht leer sein");
            }else if(testMessageTv.getText().toString().equals("")){
                testMessageTv.setError("darf nicht leer sein");
            }else {
                if (sourceEmail.equals("none") || password.equals("none") || token.equals("none")) {
                    Toast.makeText(getActivity(), "Bitte erneut einloggen!", Toast.LENGTH_SHORT).show();
                } else {
                    String targetEmail = testEmailTv.getText().toString();
                    String message = testMessageTv.getText().toString();
                    Log.d(TAG, "targetEmail: " + targetEmail + " message: " + message);
                    volleyRequest.sendMessage(sourceEmail, password, token, targetEmail, message, getActivity());
                }
            }
        });


                //final TextView textView = binding.textChats;
        //chatsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
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
            Toast.makeText(getActivity(), "NAchricht versandt", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void jsonCallbackMethod(String function, JSONObject json) {

    }
}