package com.example.supertimer.view.detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

public class DetailActivity extends AppCompatActivity {

    EditText timerName;
    Button add;
    Button start;
    ListView actionListView;
    Context context = this;
    DetailViewModel detailViewModel;
    int id = 1;
    ActionAdapter actionAdapter;
    int textSize = 16;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        super.setTheme(R.style.M_THEME);
        setContentView(R.layout.activity_detail);
        timerName = (EditText)findViewById(R.id.timerName);
        add = (Button)findViewById(R.id.addButton);
        start = (Button)findViewById(R.id.startButton);
        actionListView = (ListView)findViewById(R.id.actListView);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("id");
        }
        timerName.setText(Integer.toString(id));
//        List<Action> l= App.getInstance().getActionDao().getAllActions();
        actionAdapter = new ActionAdapter(context, R.layout.action_list_item, App.getInstance().getActionDao().findActionsByTimerId(id));
        actionListView.setAdapter(actionAdapter);
        detailViewModel = new ViewModelProvider(this).get(DetailViewModel.class);
        detailViewModel.getActionLiveData().observe(this, new Observer<List<Action>>() {
            @Override
            public void onChanged(List<Action> actions) {
                actionAdapter = new ActionAdapter(context, R.layout.action_list_item, App.getInstance().getActionDao().findActionsByTimerId(id));
                actionAdapter.setSize(textSize);
                actionListView.setAdapter(actionAdapter);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Action action = new Action();
                action.tid = id;
                action.name = "Action32";
                action.seconds = 3;
                App.getInstance().getActionDao().insert(action);
            }
        });

        timerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

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

    @Override
    protected void onStart() {
        super.onStart();
        textSize = Integer.parseInt(sharedPreferences.getString("fonts_list","2"));
        actionAdapter.setSize(textSize);
        actionListView.setAdapter(actionAdapter);
        add.setTextSize((float) (textSize / 1.4));
        start.setTextSize((float) (textSize / 1.4));
        timerName.setTextSize(textSize);
    }
}