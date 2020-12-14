package com.example.supertimer.view.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.supertimer.App;
import com.example.supertimer.R;
import com.example.supertimer.model.Timer;
import com.example.supertimer.view.detail.DetailActivity;
import com.example.supertimer.view.settings.SettingsActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    MainViewModel mainViewModel;
    Context context = this;
    ListView timerListView;
    SharedPreferences sp;
    TimerAdapter timerAdapter;
    Button addTimer;
    EditText timerName;
    float size = 1;
    String lang;
    boolean nMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInstance().ApplySettings(MainActivity.this);
        setContentView(R.layout.activity_main);

        addTimer = (Button) findViewById(R.id.addTimerButton);
        timerName = (EditText) findViewById(R.id.timerNameAdd);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        size = Float.parseFloat(sp.getString("fontSize", "1.0"));

        addTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timer t = new Timer();
                t.name = timerName.getText().toString();
                if (t.name.equals("")){
                    t.name = "New Timer";
                }
                t.color = getRandomColor();
                App.getInstance().getTimerDao().insert(t);
            }
        });

        timerAdapter = new TimerAdapter(context, R.layout.timer_list_item, App.getInstance().getTimerDao().getAllTimers());
        timerListView = (ListView) findViewById(R.id.timerListView);
        timerAdapter.setSize((int) (timerAdapter.getSize() * size));
        timerListView.setAdapter(timerAdapter);
        timerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Timer selectedTimer = (Timer) adapterView.getItemAtPosition(i);
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
                timerAdapter.setSize((int) (timerAdapter.getSize() * size));
                timerListView.setAdapter(timerAdapter);
            }
        });
    }

    public static int getRandomColor()
    {
        ArrayList<Integer> colorList = new ArrayList<Integer>();
        colorList.add(Color.GREEN);
        colorList.add(Color.BLUE);
        colorList.add(Color.YELLOW);
        colorList.add(Color.GRAY);
        colorList.add(Color.WHITE);

        Random random = new Random();
        return colorList.get(random.nextInt(colorList.size()));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem mi = menu.add(0, 1, 0, "Preferences");
        mi.setIntent(new Intent(this, SettingsActivity.class));
        return super.onCreateOptionsMenu(menu);
    }

    private void ApplySettings(){
        sp = PreferenceManager.getDefaultSharedPreferences(this);

        size = Float.parseFloat(sp.getString("fontSize", "1.0"));
        lang = sp.getString("language", "RU");
        nMode = sp.getBoolean("nightMode",  false);

        if (nMode) {
            setTheme(R.style.my_theme_dark);
        } else {
            setTheme(R.style.my_theme_light);
        }

        Configuration configuration = new Configuration();
        configuration.fontScale = size;

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        configuration.locale = locale;

        getBaseContext().getResources().updateConfiguration(configuration, null);
    }
}