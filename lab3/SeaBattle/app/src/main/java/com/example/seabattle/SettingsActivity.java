package com.example.seabattle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.seabattle.Models.AppTheme;

public class SettingsActivity extends AppCompatActivity {

    SharedPreferences sp;
    String themeVal;
    AppTheme appTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        themeVal = sp.getString("theme", "Classic");
        appTheme = AppTheme.valueOf(themeVal);
        switch (appTheme){
            case LightPink:
                setTheme(R.style.Light_pink_theme);
                break;
            case DarkPink:
                setTheme(R.style.Dark_pink_theme);
                break;
            case Dark:
                setTheme(R.style.Theme_AppCompat_NoActionBar);
                break;
            case Classic:
                setTheme(R.style.Theme_SeaBattle);
                break;
        }

        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            ListPreference listPreferenceTheme = findPreference("theme");

            listPreferenceTheme.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    getActivity().recreate();
                    return true;
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent);
//        finish();
//        super.onBackPressed();
    }
}