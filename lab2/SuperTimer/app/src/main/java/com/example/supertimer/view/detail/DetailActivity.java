package com.example.supertimer.view.detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.supertimer.App;
import com.example.supertimer.R;
import com.example.supertimer.model.Action;
import com.example.supertimer.view.main.MainViewModel;
import com.example.supertimer.view.main.TimerAdapter;
import com.example.supertimer.view.timer.TimerActivity;

import java.util.List;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    EditText timerName;
    Button add;
    Button start;
    ListView actionListView;
    EditText tNameAdd;
    Context context = this;
    DetailViewModel detailViewModel;
    int id = 1;
    ActionAdapter actionAdapter;
    float size = 1;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInstance().ApplySettings(DetailActivity.this);
        setContentView(R.layout.activity_detail);

        timerName = (EditText)findViewById(R.id.timerName);
        add = (Button)findViewById(R.id.addButton);
        start = (Button)findViewById(R.id.startButton);
        actionListView = (ListView)findViewById(R.id.actListView);
        tNameAdd = (EditText)findViewById(R.id.tNameAdd);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("id");
        }
        timerName.setText(Integer.toString(id));
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        size = Float.parseFloat(sp.getString("fontSize", "1.0"));

        actionAdapter = new ActionAdapter(context, R.layout.action_list_item, App.getInstance().getActionDao().findActionsByTimerId(id));
        actionAdapter.setSize((int) (actionAdapter.getSize() * size));
        actionListView.setAdapter(actionAdapter);
        detailViewModel = new ViewModelProvider(this).get(DetailViewModel.class);

        detailViewModel.getActionLiveData().observe(this, new Observer<List<Action>>() {
            @Override
            public void onChanged(List<Action> actions) {
                actionAdapter = new ActionAdapter(context, R.layout.action_list_item, App.getInstance().getActionDao().findActionsByTimerId(id));
                actionAdapter.setSize((int) (actionAdapter.getSize() * size));
                actionListView.setAdapter(actionAdapter);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Action action = new Action();
                action.tid = id;
                action.name = tNameAdd.getText().toString();
                if (action.name.equals("")){
                    action.name = "New Action";
                }
                action.seconds = 0;
                App.getInstance().getActionDao().insert(action);
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TimerActivity.class).putExtra("id", id);
                startActivity(intent);
            }
        });
    }
}