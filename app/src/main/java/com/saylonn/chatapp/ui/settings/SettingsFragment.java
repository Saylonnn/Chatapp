package com.saylonn.chatapp.ui.settings;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
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
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.saylonn.chatapp.R;
import com.saylonn.chatapp.comm.VolleyRequest;
import com.saylonn.chatapp.databinding.FragmentSettingsBinding;
import com.saylonn.chatapp.utils.LanguageConfig;

import java.util.Locale;

public class SettingsFragment extends Fragment {
    AppCompatActivity activity = (AppCompatActivity) getActivity();
    private final VolleyRequest volleyRequest = new VolleyRequest();
    private final String TAG = "SettingsFragment";

    private FragmentSettingsBinding binding;

    private Switch switchForDarkmode;

    private Spinner spinnerLanguage;;

    SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //SettingsViewModel settingsViewModel =
          //      new ViewModelProvider(this).get(SettingsViewModel.class);

        //binding = FragmentSettingsBinding.inflate(inflater, container, false);
        //View root = binding.getRoot();


        View root = inflater.inflate(R.layout.fragment_settings, container, false);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        switchForDarkmode = (Switch) root.findViewById(R.id.switchForDarkmode);

        switchForDarkmode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("XX", "Test");
                if (isChecked) {
                    // Dark Mode aktivieren
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    sharedPreferences.edit().putBoolean("isDarkMode", true).apply();
                } else {
                    // Dark Mode deaktivieren
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    sharedPreferences.edit().putBoolean("isDarkMode", false).apply();
                }
            }
        });

        /*spinnerLanguage = (Spinner) root.findViewById(R.id.spinnerLanguage);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.languagesArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerLanguage.setAdapter(adapter);

        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedLanguage = adapterView.getItemAtPosition(i).toString();
                String languageToLoad = null;

                Locale locale =  Locale.getDefault();

                if (selectedLanguage.equals("English")){
                    locale = Locale.ENGLISH;
                    languageToLoad = "en";
                }else if(selectedLanguage.equals("Deutsch")){
                    locale = Locale.GERMANY;
                    languageToLoad = "de";
                }
                Log.d("XXX", locale.toString());
                if (languageToLoad!= null){
                    locale = new Locale(languageToLoad);
                    Locale.setDefault(locale);
                    Configuration configuration = new Configuration();
                    configuration.setLocale(locale);
                    Context context = getContext().createConfigurationContext(configuration);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        */

        return root;
    }
}