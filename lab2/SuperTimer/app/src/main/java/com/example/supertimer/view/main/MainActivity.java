package com.example.supertimer.view.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.supertimer.App;
import com.example.supertimer.R;
import com.example.supertimer.model.Timer;
import com.example.supertimer.view.detail.DetailActivity;
import com.example.supertimer.view.settings.PrefActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    MainViewModel mainViewModel;
    Context context = this;
    ListView timerListView;
    SharedPreferences sharedPreferences;
    Timer t;
    TimerAdapter timerAdapter;
    int textSize = 16;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        super.setTheme(R.style.M_THEME);
        setContentView(R.layout.activity_main);


        t = new Timer();
        // В отдельную функцию!
        ArrayList<Integer> colorList = new ArrayList<Integer>();
        colorList.add(Color.GREEN);
        colorList.add(Color.BLUE);
        colorList.add(Color.YELLOW);
        colorList.add(Color.GRAY);
        colorList.add(Color.WHITE);
        t.name = "Timer" + (11);
        t.color = getRandomElement(colorList);

//
//        for(int i = 0; i < 10; i++){
//            t.name = "Timer" + (i+1);
//            t.color = getRandomElement(colorList);
//            App.getInstance().getTimerDao().insert(t);
//        }

        timerAdapter = new TimerAdapter(context, R.layout.timer_list_item, App.getInstance().getTimerDao().getAllTimers());
        timerListView = (ListView) findViewById(R.id.timerListView);
        timerListView.setAdapter(timerAdapter);
        timerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // получаем выбранный пункт
                Timer selectedTimer = (Timer) adapterView.getItemAtPosition(i);
//                Toast.makeText(getApplicationContext(), "Был выбран пункт id=" + selectedTimer.id,
//                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("id", selectedTimer.id);
                startActivity(intent);
            }
        });

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getTimerLiveData().observe(this, new Observer<List<Timer>>() {
            @Override
            public void onChanged(List<Timer> timers) {
                timerAdapter = new TimerAdapter(context, R.layout.timer_list_item, App.getInstance().getTimerDao().getAllTimers());
                timerAdapter.setSize(textSize);
                timerListView.setAdapter(timerAdapter);
            }
        });

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }
    public static <T> T getRandomElement(List<T> list)
    {
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem mi = menu.add(0, 1, 0, "Preferences");
        mi.setIntent(new Intent(this, PrefActivity.class));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//        if (key.equals("app_theme")) {
//            if (sharedPreferences.getBoolean("app_theme", false)){
//                //dark theme
//            }
//            else{
//                App.getInstance().getTimerDao().insert(t);
//            }
//
//        }
//        if (key.equals("fonts_list")){
//            if (sharedPreferences.getString(key, "").equals("big")){
////                timerAdapter.setSize(30);
//            }
//            else if(sharedPreferences.getString(key,"").equals("small")){
////                timerAdapter.setSize(16);
//            }
//            timerListView.setAdapter(timerAdapter);
//        }

        
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

    @Override
    protected void onStart() {
        super.onStart();
        textSize = Integer.parseInt(sharedPreferences.getString("fonts_list","2"));
        timerAdapter.setSize(textSize);
        timerListView.setAdapter(timerAdapter);
    }

}