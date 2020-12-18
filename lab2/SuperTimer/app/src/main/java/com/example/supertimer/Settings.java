package com.example.supertimer;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import java.util.Locale;

public abstract class Settings extends AppCompatActivity {
    public void ApplySettings(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        float size = Float.parseFloat(sp.getString("fontSize", "1.0"));
        String lang = sp.getString("language", "RU");
        boolean nMode = sp.getBoolean("nightMode",  false);

        if (nMode) {
            this.setTheme(R.style.my_theme_dark);
        } else {
            this.setTheme(R.style.my_theme_light);
        }

        Configuration configuration = new Configuration();
        configuration.fontScale = size;

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        configuration.locale = locale;

        this.getResources().updateConfiguration(configuration, null);
    }
}
