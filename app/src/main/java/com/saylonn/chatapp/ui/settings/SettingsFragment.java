package com.saylonn.chatapp.ui.settings;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.saylonn.chatapp.R;
import com.saylonn.chatapp.comm.VolleyRequest;
import com.saylonn.chatapp.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    AppCompatActivity activity = (AppCompatActivity) getActivity();
    private final VolleyRequest volleyRequest = new VolleyRequest();
    private final String TAG = "SettingsFragment";

    private FragmentSettingsBinding binding;

    private Switch switchDarkmode;
    private Spinner spinnerLanguages;

    private String MY_PREFS = "switch_prefs";
    private String DARKMODE_STATUS = "Darkmode on";
    private String SWITCH_STATUS = "switch_status";

    boolean switch_status;
    boolean darkmode_status;

    SharedPreferences myPreferences;
    SharedPreferences.Editor myEditor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        myPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        switchDarkmode = (Switch) binding.switchForDarkmode;

        switchDarkmode = (Switch) getActivity().findViewById(R.id.switchForDarkmode);

        myPreferences = getActivity().getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        myEditor = getActivity().getSharedPreferences(MY_PREFS,MODE_PRIVATE).edit();

        switch_status = myPreferences.getBoolean(SWITCH_STATUS, false);
        darkmode_status = myPreferences.getBoolean(DARKMODE_STATUS, false);

        /*

        switchDarkmode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    myEditor.putBoolean(SWITCH_STATUS, true);
                    myEditor.putBoolean(DARKMODE_STATUS, true);
                    myEditor.apply();
                    switchDarkmode.setChecked(true);
                }else{
                    myEditor.putBoolean(SWITCH_STATUS, false);
                    myEditor.putBoolean(DARKMODE_STATUS, false);
                    myEditor.apply();
                    switchDarkmode.setChecked(false);
                }
            }
        });
         */

        spinnerLanguages = binding.spinnerLanguage;
        /*
        spinnerLanguages = getView().findViewById(R.id.spinnerLanguage);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguages.setAdapter(adapter);
        spinnerLanguages.setOnItemSelectedListener(this);


         */

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}