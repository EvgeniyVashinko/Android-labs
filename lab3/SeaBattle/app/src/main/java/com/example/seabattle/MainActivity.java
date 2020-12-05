package com.example.seabattle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;

import com.example.seabattle.Models.AppTheme;
import com.example.seabattle.Statistics.StatisticsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    Button create, stat, connect, exit, profile, settings;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetTheme();
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mAuth = FirebaseAuth.getInstance();

        create = (Button) findViewById(R.id.create);
        connect = (Button) findViewById(R.id.connect);
        stat = (Button) findViewById(R.id.stat);
        exit = (Button) findViewById(R.id.exit);
        profile = (Button) findViewById(R.id.profile);
        settings = (Button) findViewById(R.id.settings);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateFieldActivity.class);
                intent.putExtra("mode", GameMode.Create);
                startActivity(intent);
            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateFieldActivity.class);
                intent.putExtra("mode", GameMode.Connect);
                startActivity(intent);
            }
        });

        stat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StatisticsActivity.class);
                startActivity(intent);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void SetTheme(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String themeVal = sp.getString("theme", "Classic");
        AppTheme appTheme = AppTheme.valueOf(themeVal);
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
    }
}