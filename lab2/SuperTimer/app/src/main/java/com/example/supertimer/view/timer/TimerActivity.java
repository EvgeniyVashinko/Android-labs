package com.example.supertimer.view.timer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.supertimer.App;
import com.example.supertimer.R;
import com.example.supertimer.Settings;
import com.example.supertimer.TimerService;
import com.example.supertimer.model.Action;
import com.example.supertimer.model.Timer;
import com.example.supertimer.view.main.MainActivity;

import java.util.List;

public class TimerActivity extends Settings {

    ListView actListView;
    View pause;
    TextView actionName;
    TextView time;
    TimerViewModel timerViewModel;
    int timerId = 1;
    Timer timer;
    TimerPageAdapter adapter;
    List<Action> actionList;
    TimerService timerService;
    ServiceConnection serviceConnection;
    Intent intent;
    SharedPreferences sp;

    private final String ACTION = "TIMER_ACTION";
    BroadcastReceiver br;

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(serviceConnection);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(intent,serviceConnection,0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.ApplySettings();
        setContentView(R.layout.activity_timer);

        timerViewModel = new ViewModelProvider(this).get(TimerViewModel.class);

        actListView = findViewById(R.id.actTimerListView);
        pause = findViewById(R.id.condition);
        actionName = findViewById(R.id.aName);
        time = findViewById(R.id.secView);
        sp = PreferenceManager.getDefaultSharedPreferences(this);

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                timerService = ((TimerService.MyBinder) iBinder).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };


        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                timerViewModel.setTime(intent.getExtras().getInt("currentTime"));
                actionName.setText(intent.getExtras().getString("actionName", "haha"));
            }
        };
        IntentFilter intFilt = new IntentFilter(ACTION);
        registerReceiver(br, intFilt);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            timerId = extras.getInt("id");
        }

        timer = App.getInstance().getTimerDao().findById(timerId);
        actionList = App.getInstance().getActionDao().findActionsByTimerId(timerId);

        adapter = new TimerPageAdapter(this, R.layout.timer_action_item, actionList);
        actListView.setAdapter(adapter);



        intent = new Intent(this, TimerService.class);
        intent.putExtra("timerId", timerId);
        intent.putExtra("position", 0);

        if(!timerViewModel.isServiceStart()){
            StartTimerService();
        }

        timerViewModel.getPosition().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (timerService != null){
                    timerService.setPosition(integer);
                }
            }
        });

        timerViewModel.getTime().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer)
            {
                time.setText(integer.toString());
            }
        });

        actListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                timerViewModel.setPosition(i);
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerService.TimerPause();
            }
        });
    }

    public void StartTimerService(){
        timerViewModel.setServiceStart(true);
        startService(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (timerService != null){
            timerService.TimerPause();
        }
        stopService(intent);
    }
}